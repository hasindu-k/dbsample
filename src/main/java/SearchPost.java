import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "SearchPost", urlPatterns = {"/searchpost"})
public class SearchPost extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            String product_name = request.getParameter("product_name");

            try {
                Class.forName("com.mysql.jdbc.Driver");

                String dbUrl = System.getenv("DB_URL");
                String dbUser = System.getenv("DB_USER");
                String dbPassword = System.getenv("DB_PASSWORD");


                if (dbUrl == null || dbUser == null || dbPassword == null) {
                    throw new ServletException("Database configuration not found");
                }

                Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                String sql = "SELECT product_name, product_price FROM products WHERE product_name= ?";

                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, product_name);
                    ResultSet rs = pst.executeQuery(sql);

                    while (rs.next()) {
                        String prod_name = rs.getString("product_name");
                        String prod_price = rs.getString("product_price");
                        out.println(prod_name);
                        out.println(prod_price);
                        out.println("<br/>");
                    }
                }
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            out.close();
        }
    }
}
