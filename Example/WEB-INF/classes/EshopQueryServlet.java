// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/eshopquery")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class EshopQueryServlet extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      // Print an HTML page as the output of the query
      out.println("<html>");
      out.println("<head><title>Query Response</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
        // Step 3: Execute a SQL SELECT query
        String[] authors = request.getParameterValues("author");  // Returns an array of Strings
        if (authors == null) {
         out.println("<h2>No author selected. Please go back to select author(s)</h2><body></html>");
         return; // Exit doGet()
         } 
        String sqlStr = "SELECT * FROM books WHERE author IN (";
        for (int i = 0; i < authors.length; ++i) {
           if (i < authors.length - 1) {
              sqlStr += "'" + authors[i] + "', ";  // need a commas
           } else {
              sqlStr += "'" + authors[i] + "'";    // no commas
           }
        }
        sqlStr += ") AND qty > 0 ORDER BY author ASC, title ASC";
        ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
         // Step 4: Process the query result set
         // Print the <form> start tag
         out.println("<form method='get' action='eshoporder'>");
         
         // For each row in ResultSet, print one checkbox inside the <form>
                        <table>
                 <tr>
                   <th>heading for column 1</th>
                   <th>heading for column 2</th>
                   ......
                 </tr>
                 <tr>
                   <td>data for column 1</td>
                   <td>data for column 2</td>
                   ......
                 </tr>
                 ......
               </table>
         out.println("</table>");
         while(rset.next()) {
            out.println("<tr><input type='checkbox' name='id' value="
                  + "'" + rset.getString("id") + "' />"
                  + rset.getString("author") + ", "
                  + rset.getString("title") + ", $"
                  + rset.getString("price") + "<tr>");
         }
         out.println("</table>");
         // Print the submit button and </form> end-tag
         out.println("<p>Enter your Name: <input type='text' name='cust_name' /></p>");
         out.println("<p>Enter your Phone Number: <input type='text' name='cust_phone' /></p>");
         out.println("<p>Enter your Email: <input type='text' name='cust_email' /></p>");
         out.println("<p><input type='submit' value='ORDER' />");
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