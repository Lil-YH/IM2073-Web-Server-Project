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

      // Print an HTML page as the output of the query
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head><title>Fast Food Kings Menu</title></head>");
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
         sqlStr += ") AND qty > 0 ORDER BY id ASC";

         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
         // Print the <form> start tag
         out.println("<form method='get' action='ffsorder'>");

         out.println("<h1>Fast Food Kings<h1>");

         out.println("<style>table, th, td {border:1px solid black;}</style>"
                  + "<table>"
                  + "<tr>"
                  + "<th> </th>"
                  + "<th>QUANTITY</th>"
                  + "<th>CATEGORY</th>"
                  + "<th>ITEM</th>"
                  + "<th>CALORIES (cal)</th>"
                  + "<th>PRICE</th>"
                  + "</tr>");
         
         // For each row in ResultSet, print one checkbox inside the <form>
         while(rset.next()) {
            out.println("<tr>"
                     + "<td><input type='checkbox' name='id' value='" + rset.getString("id") + "' /></td>"
                     + "<td><input type='number' name='qty' value='0' /></td>"
                     + "<td>" + rset.getString("foodType") + "</td>"
                     + "<td>" + rset.getString("foodItem") + "</td>"
                     + "<td>" + rset.getString("calories") + "</td>"
                     + "<td>$" + rset.getString("price") + "</td>"
                     + "</tr>");
         }

         out.println("</table>");
 
         out.println("<p>Enter your Name: <input type='text' name='cust_name' /></p>");
         out.println("<p>Enter your Email: <input type='text' name='cust_email' /></p>");
         out.println("<p>Enter your Phone Number: <input type='text' name='cust_phone' /></p>");

         // Print the submit + clear button and </form> end-tag
         out.println("<br><br>");
         out.println("<input type='reset' value='CLEAR' />");
         out.println("<input type='submit' value='ORDER' />");
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