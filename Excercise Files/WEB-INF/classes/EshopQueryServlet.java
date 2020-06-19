// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

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
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head><title>Query Response</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "ams", "momin");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3: Execute a SQL SELECT query
         String sqlStr = "select * from books where author = "
               + "'" + request.getParameter("author") + "'"   // Single-quote SQL string
               + " and qty > 0 order by price desc";

         out.println("<h3>Thank you for your query.</h3>");
         out.println("<p>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
         out.println("<form method='get' action='eshoporder'>");
// For each row in ResultSet, print one checkbox inside the <form>


         out.println("<table>");
			out.println("<tr>");
				out.println("<th>SELECT</th>");
            out.println("&nbsp&nbsp&nbsp&nbsp&nbsp");
				out.println("<th>AUTHOR</th>");
				out.println("<th>TITLE</th>");
				out.println("<th>PRICE</th>");
			out.println("</tr>");
			while(rset.next())
			{
				out.println("<tr>");
					out.println("<td><input type='checkbox' name='id' value=" + "'" + rset.getString("id") + "' /></td>");
               out.println("&nbsp&nbsp&nbsp&nbsp&nbsp");
					out.println("<td>"+ rset.getString("author") + "</td>");
					out.println("<td>"+ rset.getString("title") + "</td>");
					out.println("<td>"+ rset.getString("price") + "</td>");
				out.println("</tr>");
			}
		out.println("</table>");



		// while(rset.next()) {
		// 	out.println("<p><input type='checkbox' name='id' value="
		// 	+ "'" + rset.getString("id") + "' />"
		// 	+ rset.getString("author") + ", "
		// 	+ rset.getString("title") + ", $"
		// 	+ rset.getString("price") + "</p>");
		// 	}
			// Print the submit button and </form> end-tag
			out.println("<p>Enter your Name: <input type='text' name='cust_name' /></p>");
			out.println("<p>Enter your Email: <input type='text' name='cust_email' /></p>");
			out.println("<p>Enter your Phone Number: <input type='text' name='cust_phone' /></p>");

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