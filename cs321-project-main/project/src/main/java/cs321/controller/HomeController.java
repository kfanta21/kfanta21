package cs321.project.controller;
import cs321.repository.UserRepository;
import cs321.model.User;
import java.sql.*;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/StudentDashboard")
    public String studentDashboard() {
        return "StudentDashboard";
    }

    @GetMapping("/ProfDashboard")
    public String professorDashboard() {
        return "ProfDashboard";
    }


    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("username") != null) {
            // User is already logged in, redirect them to the dashboard
            return "redirect:/StudentDashboard";
        }
        // User is not logged in, show the login page
        return "login";
    }

    @GetMapping("/courses")
    public String courses(HttpServletRequest request) {
        return "courses";
    }

    @GetMapping("/tasks")
    public String tasks(HttpServletRequest request) {
        return "tasks";
    }

    @GetMapping("/Settings")
    public String Settings(HttpServletRequest request) {
        return "Settings";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        // Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Redirect to the login page
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String handleSignup(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC";
        String dbUsername = "root";
        String dbPassword = "Admin123!";

        try {
            // Create a database connection
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Check if the user already exists in the database
            String checkQuery = "SELECT * FROM User WHERE username = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, username);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                // User already exists, redirect back to the signup page with an error message
                return "redirect:/signup?error=user_exists";
            } else {
                // User doesn't exist, create a new entry in the database
                String insertQuery = "INSERT INTO User (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, username);
                insertStatement.setString(2, password);
                insertStatement.setString(3, userType);
                insertStatement.executeUpdate();

                // Set the user as authenticated in the session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                return "redirect:/logout"; // Redirect to the dashboard page after successful signup
            }
        } catch (SQLException e) {
            System.out.println("die");
            // Handle any potential database errors
            e.printStackTrace();
            // Redirect to an error page or handle the error appropriately
            return "redirect:/error";
        }
    }

    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    @ResponseBody
    public String handleLogin(HttpServletRequest request, HttpServletResponse response) {
        String user_email = request.getParameter("username");
        String password = request.getParameter("password");

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC";
        String dbUsername = "root";
        String dbPassword = "Admin123!";

        try {
            // Create a database connection
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Prepare the SQL query
            String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user_email);
            statement.setString(2, password);

            // Execute the query and retrieve the result
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // User found, set the user as authenticated in the session
                HttpSession session = request.getSession();
                session.setAttribute("username", resultSet.getString("username"));

                String role = resultSet.getString("role");
                // Return "success" so JavaScript can redirect to the dashboard
                return "success:" + role;
            } else {
                // Invalid credentials, return "error" so JavaScript can make the container shake
                return "error:invalid_credentials";
            }
        } catch (SQLException e) {
            // Handle any potential database errors
            e.printStackTrace();

            // Return "error" so JavaScript can make the container shake
            return "error:database_error";
        }
    }
}