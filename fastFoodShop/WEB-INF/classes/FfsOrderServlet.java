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
         Statement stmt = conn.createStatement();
      ) {
	      // Step 3 & 4: Execute a SQL SELECT query and Process the query result
	      // Retrieve the food's id. Can order more than one food.
         String sqlStr;
         sqlStr = "SELECT * FROM food RIGHT JOIN cart ON food.id = cart.id WHERE cart.qty_ordered > 0 ORDER by cart.id ASC";
	      if (true) {
         	int count;
         // Process each of the food

         out.println("<h1 style='text-align:center;'>Fast Food Kings</h1>");
         out.println("<h3 style='text-align:center;'>Order placed</h3>");
         out.println("<p style='text-align:center;'>We are preparing your order.</p>");
         //out.println("Test before first");
         //rset2.beforeFirst();
          
         out.println("<table class='table'>"
               + "<tr>"
               + "<th>CATEGORY</th>"
               + "<th>ITEM</th>"
               + "<th>CALORIES (cal)</th>"
               + "<th>QUANTITY ORDERED</th>"
               + "<th>PRICE</th>"
               + "</tr>");					// why i = 0 here? shld it be set inside while loop? then i++ before the end of the loop
         float totalPrice = 0;
         int totalCalories = 0;
         int x =0;
         ResultSet rset2 = stmt.executeQuery(sqlStr);
         while(rset2.next()) {
          	out.println("<tr>"
                  //+ "<td>" + qtys[i] + "</td>"	// do we need this?
                  + "<td>" + rset2.getString("foodType") + "</td>"
                  + "<td>" + rset2.getString("foodItem") + "</td>"
                  + "<td>" + rset2.getString("calories") + "</td>"
                  + "<td>" + rset2.getString("qty_ordered") + "</td>"
                  + "<td>$" + rset2.getString("price") + "</td>"
                  + "</tr>");
          	totalCalories += rset2.getInt("qty_ordered") * rset2.getInt("calories");
          	totalPrice += rset2.getInt("qty_ordered") * rset2.getFloat("price");
            x++;
         }
         out.println("</table>");
         for (int i = 0; i < x-1; i++){
            String qty_ordered = rset2.getString("qty_ordered");
            String idString = rset2.getString("id");
            //out.println("Test loop 1");
            sqlStr = "UPDATE food SET qty = qty - " + qty_ordered + " WHERE id = " + idString;
            //out.println("<p>" + sqlStr + "</p>");  // for debugging
            count = stmt.executeUpdate(sqlStr);
            //out.println("<p>" + count + " record updated.</p>");
            //out.println("Test loop 2");
            // Create a transaction record
            sqlStr = "INSERT INTO order_records (id, qty_ordered) VALUES ("
                   + idString + ", " + qty_ordered + ")";
            out.println("<p>" + sqlStr + "</p>");  // for debugging
            count = stmt.executeUpdate(sqlStr);
            /*out.println("<p>" + count + " record inserted.</p>");
            out.println("<h3>Your order for food id=" + ids[i]
                  + " has been confirmed.</h3>");*/

            // clear cart
            
            //out.println("Test loop 3");
         	//out.println("Test loop 2");
         }
         sqlStr = "UPDATE cart SET qty_ordered = 0";
         count = stmt.executeUpdate(sqlStr);
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