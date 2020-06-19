// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/homequery")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class home extends HttpServlet {

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
         out.println("<title>Author Name</title>");
         out.println("<link rel='stylesheet' type='text/css' href='css/index.css'>");
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
        
		String sqlStr = "SELECT DISTINCT author FROM books WHERE qty > 0 GROUP BY author";


    out.println("<header>");
		out.println("<nav id='header-nav'>");
			out.println("<div>");
				out.println("<h1>WelCome to BookShelf</h1>");
				out.println("<a href='contact.html' name='link to Contact Us page'><span>Contact Us</span></a>");
				out.println("<a href='index.html' name='link to home page'><span>Home</span></a>");
			out.println("</div>");
		out.println("</nav>");
	out.println("</header>");

         //out.println("<p id='thank'>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
// For each row in ResultSet, print one checkbox inside the <form>

        out.println("<div class='contact-form'>");
        out.println("<form id='contact-form' method='post' action='eshopquery'>");
			out.println("<p>Choose authors:</p>");
			out.println("<p>");
			while(rset.next())
            {
             	out.println("<input type='checkbox' name='author' class='frm_inpt' value=" + "'" + rset.getString("author") + "' />");
    			out.println(rset.getString("author"));
    			out.println("&nbsp&nbsp&nbsp&nbsp&nbsp");
            }
    		out.println("</p>");
 
        out.println("<br>");
        out.println("<hr>");
        out.println("<br>");


        out.println("<p>Choose a price range:</p>");
		out.println("<p><input type='radio' name='price' value='50' checked />less than $50");
		out.println("&nbsp&nbsp&nbsp&nbsp&nbsp");
		out.println("<input type='radio' name='price' value='100' />less than $100</p>");

		out.println("<input type='reset' class='submit' value='Clear'>");
 		out.println("<input type='submit' class='submit' value='Search'>");
            
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