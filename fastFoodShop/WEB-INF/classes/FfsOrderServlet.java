// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;             // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;             // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/ffsorder")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class FfsOrderServlet extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();

      // Print an HTML page as the output of the query
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head><title>Fast Food Kings Cart</title><link rel = 'stylesheet' href = 'style.css'>");
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
	         out.println("<i class='bi-cart-fill me-1'></i>");
	         out.println("<input type='submit' value='View Cart' class='btn btn-outline-dark' >");
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
         Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ) {
	      // Step 3 & 4: Execute a SQL SELECT query and Process the query result
	      // Retrieve the food's id. Can order more than one food.
	      String[] ids = request.getParameterValues("id");
	      String[] qtys = request.getParameterValues("qty");
         String sqlStr;
         sqlStr = "SELECT * FROM food RIGHT JOIN cart ON food.id = cart.id WHERE cart.qty_ordered > 0 ORDER by cart.id ASC";
         ResultSet rset = stmt.executeQuery(sqlStr);
	      if (rset.next()) {
         	int count;
            rset.beforeFirst();
            int i = 0 ;
         // Process each of the food
         while(rset.next()) {
            // Update the qty of the table food
            sqlStr = "UPDATE food SET qty = qty - " + rset.getString("qty_ordered") + " WHERE id = " + rset.getString("id");
            //out.println("<p>" + sqlStr + "</p>");  // for debugging
            count = stmt.executeUpdate(sqlStr);
            //out.println("<p>" + count + " record updated.</p>");

            // Create a transaction record
            sqlStr = "INSERT INTO order_records (id, qty_ordered) VALUES ("
                   + rset.getString("qty_ordered") + ", " + rset.getString("id") + ")";
            //out.println("<p>" + sqlStr + "</p>");  // for debugging
            count = stmt.executeUpdate(sqlStr);
            /*out.println("<p>" + count + " record inserted.</p>");
            out.println("<h3>Your order for food id=" + ids[i]
                  + " has been confirmed.</h3>");*/

            // clear cart
            sqlStr = "DELETE FROM cart";
            count = stmt.executeUpdate(sqlStr);
            i++;
         }

         out.println("<h1 style='text-align:center;'>Fast Food Kings</h1>");
         out.println("<h3 style='text-align:center;'>Order placed</h3>");
         out.println("<p style='text-align:center;'>We are preparing your order.</p>");

         rset.beforeFirst();
          
         out.println("<style>table, th, td {border:1px solid black;}</style>"
               + "<table>"
               + "<tr>"
               + "<th>CATEGORY</th>"
               + "<th>ITEM</th>"
               + "<th>CALORIES (cal)</th>"
               + "<th>QUANTITY ORDERED</th>"
               + "<th>PRICE</th>"
               + "</tr>");					// why i = 0 here? shld it be set inside while loop? then i++ before the end of the loop
         float totalPrice = 0;
         int totalCalories = 0;
         while(rset.next()) {
         	// int i = 0;
          	out.println("<tr>"
                  //+ "<td>" + qtys[i] + "</td>"	// do we need this?
                  + "<td>" + rset.getString("foodType") + "</td>"
                  + "<td>" + rset.getString("foodItem") + "</td>"
                  + "<td>" + rset.getString("calories") + "</td>"
                  + "<td>" + rset.getString("qty_ordered") + "</td>"
                  + "<td>$" + rset.getString("price") + "</td>"
                  + "</tr>");
          	totalCalories += rset.getInt("qty_ordered") * rset.getInt("calories");
          	totalPrice += rset.getInt("qty_ordered") * rset.getFloat("price");
         }

       	out.println("<h3>Total Calories: "+ totalCalories + "Cal</h3>");
       	out.println("<h3>Total Price: $"+ totalPrice + "</h3>");
       	out.println("<br><br>");
       	out.println("<h3 style='text-align:center;'>Thank you!</h3>");
       	// insert button back to home page

         } else { // No food selected
         	out.println("<h3>No food selected... Please go back and order a food/drink :)</h3>");
         }
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}