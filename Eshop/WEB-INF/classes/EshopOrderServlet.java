// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/eshoporder")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class EshopOrderServlet extends HttpServlet {

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
      out.println("</head>");
      out.println("<body>");

      out.println("<header>");
      out.println("<nav id='header-nav'>");
         out.println("<div>");
            out.println("<h1>WelCome to BookShelf</h1>");
            out.println("<a href='contact.html' name='link to Contact Us page'><span>Contact Us</span></a>");
            out.println("<a href='index.html' name='link to home page'><span>Home</span></a>");
         out.println("</div>");
      out.println("</nav>");
   out.println("</header>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "ams", "momin");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"



         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {

            String cust_name = request.getParameter("cust_name");
            String cust_email = request.getParameter("cust_email");
            String cust_phone = request.getParameter("cust_phone");
            String[] ids = request.getParameterValues("id");
            if (ids != null)
            {
               String sqlStr;
               int count;

               out.println("<p><h1 style='margin: 50px 0px 30px 40%;'>" + cust_name + "</h1></p>");
               for (int i = 0; i < ids.length; ++i)
               {
                  sqlStr = "UPDATE books SET qty = qty - 1 WHERE id = " + ids[i];
                  //out.println("<p>" + sqlStr + "</p>");
                  count = stmt.executeUpdate(sqlStr);
                  //out.println("<p>" + count + " record updated.</p>");

                  
                  sqlStr = "INSERT INTO order_records VALUES ("+ ids[i] + ", 1,'" + cust_name + "','" + cust_email + "','" + cust_phone + "')";
                  count = stmt.executeUpdate(sqlStr);
                  out.println("<p style='clear: both;'><h3 style='margin: 10px 0px 30px 35%;'>Your order for book id=" + ids[i] + " has been confirmed.</h3></p>");
               }
               out.println("<p><h2 style='margin: 50px 0px 30px 45%;'>Thank you.</h2></p>");
         }
         else
         {
            out.println("<h3 style='margin: 50px 0px 30px 40%;'>Please go back and select a book...</h3>");
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