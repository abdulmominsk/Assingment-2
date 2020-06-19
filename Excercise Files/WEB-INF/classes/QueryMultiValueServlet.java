// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryMultiValueServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/querymv")
public class QueryMultiValueServlet extends HttpServlet {

  
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
     
      response.setContentType("text/html");
    
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
               "ams", "momin");  
             

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3: Execute a SQL SELECT query
	String[] authors = request.getParameterValues("author");
	if (authors == null)
	{
		out.println("<h2>No author selected. Please go back to select author(s)</h2><body></html>");
		return;
	}
	String sqlStr = "SELECT * FROM books WHERE author IN (";
	for (int i = 0; i < authors.length; ++i)
	{
		if (i < authors.length - 1)
		{
			sqlStr += "'" + authors[i] + "', "; 
		}
		else
		{
			sqlStr += "'" + authors[i] + "'"; 
		}
	}
	sqlStr += ") AND qty > 0 ORDER BY author ASC, title ASC";

         out.println("<h3>Thank you for your query.</h3>");
         out.println("<p>Your SQL statement is: " + sqlStr + "</p>"); 
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
         int count = 0;
         while(rset.next()) {
            // Print a paragraph <p>...</p> for each record
            out.println("<p>" + rset.getString("author")
                  + ", " + rset.getString("title")
                  + ", $" + rset.getDouble("price") + "</p>");
            count++;
         }
         out.println("<p>==== " + count + " records found =====</p>");
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}