// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;             // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;             // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/ffsquery")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class FfsQueryServlet extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      HttpSession session =request.getSession();         
      // Print an HTML page as the output of the query
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head><title>Fast Food Kings Menu</title>");
      out.println("<meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no' />"); 
      out.println("<meta name='description' content='' />");
      out.println("<meta name='author' content='' />");
      out.println("<title>Shop Homepage - Start Bootstrap Template</title>");
      out.println("<!-- Bootstrap icons-->");
      out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css' rel='stylesheet' />");
      
      
      
      
      
      
      
      
      out.println("<!-- Core theme CSS (includes Bootstrap)-->");
      out.println("<link href='css/styles.css' rel='stylesheet' /></head>");

      out.println("<body> <nav class='navbar navbar-expand-lg navbar-light bg-light'>");
      out.println("<div class='container px-4 px-lg-5'>");
          out.println("<a class='navbar-brand' href='#!'>FAST FOOD KINGS</a>");
          out.println("<button class='navbar-toggler' type='button' data-bs-toggle='collapse' data-bs-target='#navbarSupportedContent' aria-controls='navbarSupportedContent' aria-expanded='false' aria-label='Toggle navigation'><span class='navbar-toggler-icon'></span></button>");
          out.println("<div class='collapse navbar-collapse' id='navbarSupportedContent'>");
              out.println("<ul class='navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4'>");
                  out.println("<li class='nav-item'><a class='nav-link active' aria-current='page' href='index.html'>Home</a></li>");
              out.println("</ul>");
              out.println("<form class='d-flex' method='get' action='ffscart'>");
                        out.println("<input type='submit' value='View Cart' class='btn btn-outline-dark' >");
                            out.println("<i class='bi-cart-fill me-1'></i>");
                           
                        out.println("</input>");
                   out.println(" </form>");
          out.println("</div>");
      out.println("</div>");
 out.println(" </nav>");
 out.println(" <header class='bg-dark py-5'>");
            out.println("<div class='container px-4 px-lg-5 my-5'>");
               out.println(" <div class='text-center text-white'>");
                    out.println("<h1 class='display-4 fw-bolder'>FILL YOUR TUMMY</h1>");
                   out.println(" <p class='lead fw-normal text-white-50 mb-0'>With our amazing western delights!</p>");
               out.println(" </div>");
            out.println("</div>");
       out.println(" </header>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/fastfoodshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3: Execute a SQL SELECT query
         String[] foodTypes = request.getParameterValues("foodType");  // Returns an array of Strings
         
         if (foodTypes == null) {
            out.println("<h2>No foodType selected. Please go back to select foodType(s)</h2><body></html>");
            return; // Exit doGet()
         }

         String sqlStr = "SELECT * FROM food WHERE foodType IN (";
         for (int i = 0; i < foodTypes.length; ++i) {
            if (i < foodTypes.length - 1) {
               sqlStr += "'" + foodTypes[i] + "', ";  // need a commas
            } else {
               sqlStr += "'" + foodTypes[i] + "'";    // no commas
            }
         }
         sqlStr += ") ORDER BY id ASC";

         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
         // Print the <form> start tag
         out.println("<form method='get' action='ffscart'>");

         out.println("<h1>Fast Food Kings<h1>");

         out.println("<table class='table'>"
                  + "<tr>"
                  + "<th> </th>"
                  + "<th>QUANTITY</th>"
                  + "<th>CATEGORY</th>"
                  + "<th>ITEM</th>"
                  + "<th>CALORIES (cal)</th>"
                  + "<th>PRICE</th>"
                  + "<th> </th>"
                  + "</tr>");
         
         // For each row in ResultSet, print one checkbox inside the <form>
        
         while(rset.next()) {
            
            if(rset.getInt("qty") > 0) {      //Check if item is sold out and display accordingly
               out.println("<tr>"
               + "<td><input type='checkbox' name='id' value='" + rset.getString("id") + "' /></td>"
               + "<td><input type='number' name='qty' value='0' max='" + rset.getInt("qty") + "' /></td>" //Restrict qty able to be ordered to be =< stock
               + "<td>" + rset.getString("foodType") + "</td>"
               + "<td>" + rset.getString("foodItem") + "</td>"
               + "<td>" + rset.getString("calories") + "</td>"
               + "<td>$" + rset.getString("price") + "</td>"
               + "<td><image width='150' src='images/" + rset.getString("foodImage") + "'/></td>"
               + "</tr>");   
            } else {
               out.println("<tr>"
               + "<td><input type='checkbox' name='id' value='" + rset.getString("id") + "' /></td>"
               + "<td><input type='number' name='qty' value='0' max='" + rset.getInt("qty") + "' /></td>" //Restrict qty able to be ordered to be =< stock
               + "<td>" + rset.getString("foodType") + "</td>"
               + "<td style='color: red'>" + rset.getString("foodItem") + "(SOLDOUT)</td>"
               + "<td>" + rset.getString("calories") + "</td>"
               + "<td>$" + rset.getString("price") + "</td>"
               + "<td><image width='150' src='images/" + rset.getString("foodImage") + "'/></td>"
               + "</tr>");
            }
         }

         out.println("</table>");

         // Print the submit + clear button and </form> end-tag
         out.println("<br><br>");
         out.println("<input class='btn btn-outline-dark mt-auto' type='reset' value='CLEAR' />");
         out.println("<input class='btn btn-outline-dark mt-auto' type='submit' value='UPDATE CART' />");
         out.println("</form>");
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}