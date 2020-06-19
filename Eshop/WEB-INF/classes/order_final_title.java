// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/eshopquerytitle")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class order_final_title extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();

      // Print an HTML page as the output of the query
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
         out.println("<title>Query Response</title>");
         out.println("<link rel='stylesheet' type='text/css' href='css/index.css'>");
         out.println("<link rel='stylesheet' type='text/css' href='css/order.css'>");
      out.println("</head>");
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
        String[] titles = request.getParameterValues("title");

		if (titles == null)
		{
			out.println("<h2>No book(s) selected. Please go back to select book(s)</h2><body></html>");
			return;
		}
   
		String sqlStr = "SELECT * FROM books WHERE title IN (";
		for (int i = 0; i < titles.length; ++i)
		{
			if (i < titles.length - 1)
			{
				sqlStr += "'" + titles[i] + "', "; 
			}
			else
			{
				sqlStr += "'" + titles[i] + "'"; 
			}
		}
		sqlStr += ") AND qty > 0 ORDER BY author ASC, title ASC";
    


    out.println("<header>");
		out.println("<nav id='header-nav'>");
			out.println("<div>");
				out.println("<h1>WelCome to BookShelf</h1>");
				out.println("<a href='contact.html' name='link to Contact Us page'><span>Contact Us</span></a>");
				out.println("<a href='index.html' name='link to home page'><span>Home</span></a>");
			out.println("</div>");
		out.println("</nav>");
	out.println("</header>");

         out.println("<h2>Thank you for your query.</h2>");
         //out.println("<p id='thank'>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
// For each row in ResultSet, print one checkbox inside the <form>
         out.println("<div class='contact-form'>");
         out.println("<form id='contact-form' method='post' action='eshoporder'>");
            out.println("<table>");
               out.println("<tr>");
                 out.println("<th>SELECT</th>");
                 out.println("<th>AUTHOR</th>");
                 out.println("<th>TITLE</th>");
                 out.println("<th>PRICE</th>");
            out.println("</tr>");
            while(rset.next())
            {
              out.println("<tr>");
                 out.println("<td><input type='checkbox' name='id' value=" + "'" + rset.getString("id") + "' /></td>");
                 out.println("<td>"+ rset.getString("author") + "</td>");
                 out.println("<td>"+ rset.getString("title") + "</td>");
                 out.println("<td>"+ rset.getString("price") + "</td>");
              out.println("</tr>");
            }
            out.println("</table>");


         out.println("<input name='cust_name' type='text' class='form-control' placeholder='Your Name' required>");
         out.println("<br>");
         out.println("<input name='cust_email' type='email' class='form-control' placeholder='Your Email' required>");
         out.println("<br>");
         out.println("<input name='cust_phone' type='text' class='form-control' placeholder='Your Phone' required>");
         out.println("<br>");

         out.println("<input type='reset' class='submit' value='Clear'>");
         out.println("<input type='submit' class='submit' value='Place Order'>");

			out.println("</form>");
      out.println("</div>");
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}