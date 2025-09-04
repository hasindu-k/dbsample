import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;

@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

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
            String username = request.getParameter("username");
            String password = request.getParameter("password");

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

                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, username);
                    pst.setString(2, password);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        response.sendRedirect("search.jsp");
                    } else {
                        out.println("Invalid username and/or password");
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
