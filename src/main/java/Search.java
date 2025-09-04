import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;

@WebServlet(name = "Search", urlPatterns = {"/search"})
public class Search extends HttpServlet {

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
                Properties props = new Properties();
                props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
                String dbUrl = props.getProperty("db.url");
                String dbUser = props.getProperty("db.user");
                String dbPassword = props.getProperty("db.password");

                if (dbUrl == null || dbUser == null || dbPassword == null) {
                    throw new ServletException("Database configuration not found");
                }

                Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                String sql = "SELECT product_name, product_price FROM products WHERE product_name = ? ";

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
