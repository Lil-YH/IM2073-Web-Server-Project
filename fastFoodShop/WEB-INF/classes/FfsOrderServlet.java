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
      out.println("<head><title>Fast Food Kings Order Confirmation</title></head>");
      out.println("<body>");

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
         String[] ids = request.getParameterValues("id");
         if (ids != null) {
            String sqlStr;
            int count;
 
            // Process each of the food
            for (int i = 0; i < ids.length; ++i) {
               // Update the qty of the table food
               sqlStr = "UPDATE food SET qty = qty - 1 WHERE id = " + ids[i];
               //out.println("<p>" + sqlStr + "</p>");  // for debugging
               count = stmt.executeUpdate(sqlStr);
               //out.println("<p>" + count + " record updated.</p>");
 
               // Create a transaction record
               sqlStr = "INSERT INTO order_records (id, qty_ordered) VALUES ("
                     + ids[i] + ", 1)";
               //out.println("<p>" + sqlStr + "</p>");  // for debugging
               count = stmt.executeUpdate(sqlStr);
               /*out.println("<p>" + count + " record inserted.</p>");
               out.println("<h3>Your order for food id=" + ids[i]
                     + " has been confirmed.</h3>");*/
            }

            out.println("<h1 style='text-align:center;'>Fast Food Kings</h1>");
            out.println("<h3 style='text-align:center;'>Order placed</h3>");
            out.println("<p style='text-align:center;'>We are preparing your order.</p>");
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