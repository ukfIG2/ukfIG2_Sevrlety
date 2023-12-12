package sk;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@WebServlet("/Chat")
public class Chat extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mysql://localhost/chatRV";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
       

        if ("login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (authenticateUser(username, password)) {
                HttpSession session = request.getSession(true); 
                session.setAttribute("username", username);
                showHomePage(response, username);
            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><head><title>Login</title></head><body>");
                out.println("<h2>Login</h2>");
                out.println("<p>Incorrectly entered data</p>");
                out.println("<form method=\"post\" action=\"Chat?action=login\">");
                out.println("Username: <input type=\"text\" name=\"username\"><br>");
                out.println("Password: <input type=\"password\" name=\"password\"><br>");
                out.println("<input type=\"submit\" value=\"Login\">");
                out.println("</form>");
                out.println("</body></html>");
            }
        }else if ("home".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("username") != null) {
                String username = (String) session.getAttribute("username");
                showHomePage(response, username);
                
            } else {
                response.sendRedirect(request.getContextPath() + "/Chat");
            }
        }
        
        else if ("addMessage".equals(action)) {
            String newMessage = request.getParameter("newMessage");
            String username = (String) request.getSession().getAttribute("username");
            addMessage(username, newMessage, response, request);
        }
        else if ("ban".equals(action)) {
            String banningUsername = request.getParameter("banningUsername");
            String bannedUsername = request.getParameter("bannedUsername");
            banUser(banningUsername, bannedUsername, response, request);
        }
        else if ("unban".equals(action)) {
            String banningUsername = request.getParameter("banningUsername");
            String unbannedUsername = request.getParameter("bannedUsername");
            unbanUser(banningUsername, unbannedUsername, response, request);
        }

        else if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/Chat");
        }


    }


    private boolean authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("username") != null) {
            response.sendRedirect(request.getContextPath() + "/Chat?action=home");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Login</title></head><body>");
        out.println("<h2>Login</h2>");
        out.println("<form method=\"post\" action=\"Chat?action=login\">");
        out.println("Username: <input type=\"text\" name=\"username\"><br>");
        out.println("Password: <input type=\"password\" name=\"password\"><br>");
        out.println("<input type=\"submit\" value=\"Login\">");
        out.println("</form>");
        out.println("</body></html>");
    }


    private void showHomePage(HttpServletResponse response, String username) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Home</title></head><body>");
        out.println("<h2>Welcome, " + username + "!</h2>");

        displayMessages(out, username);

        out.println("<h3>Add a Message:</h3>");
        out.println("<form method=\"post\" action=\"Chat?action=addMessage\">");
        out.println("Message: <input type=\"text\" name=\"newMessage\"><br>");
        out.println("<input type=\"submit\" value=\"Send\">");
        out.println("</form>");

        List<String> bannedUsers = getBannedUsers(username);
        if (!bannedUsers.isEmpty()) {
            out.println("<p>You have banned users: " + String.join(", ", bannedUsers) + "</p>");
            for (String bannedUser : bannedUsers) {
                out.println("<form method=\"post\" action=\"Chat?action=unban\">");
                out.println("<input type=\"hidden\" name=\"banningUsername\" value=\"" + username + "\">");
                out.println("<input type=\"hidden\" name=\"bannedUsername\" value=\"" + bannedUser + "\">");
                out.println("<input type=\"submit\" value=\"Unban " + bannedUser + "\">");
                out.println("</form>");
            }
        }

        out.println("<form method=\"post\" action=\"Chat?action=logout\">");
        out.println("<input type=\"submit\" value=\"Logout\">");
        out.println("</form>");
        out.println("</body></html>");
    }


    private void displayMessages(PrintWriter out, String username) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM chat_messages WHERE username NOT IN " +
                           "(SELECT banned_user_id FROM banned_users WHERE banning_user_id = " +
                           "(SELECT id FROM users WHERE username = ?));";// ORDER BY chat_messages.message_date";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    out.println("<h3>Messages:</h3>");
                    out.println("<ul>");

                    while (resultSet.next()) {
                        String messageUsername = resultSet.getString("username");
                        String message = resultSet.getString("message");
                        Timestamp messageDate = resultSet.getTimestamp("message_date");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = dateFormat.format(messageDate);

                        if (!username.equals(messageUsername)) {
                            out.println("<li><strong>" + formattedDate + " - user: " + messageUsername + ":</strong> " + message +
                                    getBanButton(messageUsername, username) + "</li>");
                        } else {
                            out.println("<li><strong>" + formattedDate + " - user: " + messageUsername + ":</strong> " + message + "</li>");
                        }
                    }

                    out.println("</ul>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String getBanButton(String messageUsername, String banningUsername) {
        if (!isUserBanned(messageUsername, banningUsername)) {
            return "<form method=\"post\" action=\"Chat?action=ban\">" +
                    "<input type=\"hidden\" name=\"banningUsername\" value=\"" + banningUsername + "\">" +
                    "<input type=\"hidden\" name=\"bannedUsername\" value=\"" + messageUsername + "\">" +
                    "<input type=\"submit\" value=\"Ban\">" +
                    "</form>";
        } else {
            return "<p>User already banned</p>";
        }
    }

    private boolean isUserBanned(String bannedUsername, String banningUsername) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM banned_users WHERE banning_user_id = " +
                           "(SELECT id FROM users WHERE username = ?) AND banned_user_id = " +
                           "(SELECT id FROM users WHERE username = ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, banningUsername);
                preparedStatement.setString(2, bannedUsername);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    
    private void addMessage(String username, String newMessage, HttpServletResponse response, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("username") != null) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String query = "INSERT INTO chat_messages (username, message) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, newMessage);
                    preparedStatement.executeUpdate();
                }
                System.out.println("Message added successfully");

                try {
                    showHomePage(response, (String) session.getAttribute("username"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User not authenticated");
            response.sendRedirect(request.getContextPath() + "/Chat");
        }
    }
    private void banUser(String banningUsername, String bannedUsername, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("username") != null) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String query = "INSERT INTO banned_users (banning_user_id, banned_user_id) VALUES " +
                               "((SELECT id FROM users WHERE username = ?), (SELECT id FROM users WHERE username = ?))";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, banningUsername);
                    preparedStatement.setString(2, bannedUsername);
                    preparedStatement.executeUpdate();
                    try {
                        showHomePage(response, (String) session.getAttribute("username"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User not authenticated");
            
        }
    }
    private List<String> getBannedUsers(String banningUsername) {
        List<String> bannedUsers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT username FROM users WHERE id IN " +
                           "(SELECT banned_user_id FROM banned_users WHERE banning_user_id = " +
                           "(SELECT id FROM users WHERE username = ?))";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, banningUsername);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        bannedUsers.add(resultSet.getString("username"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bannedUsers;
    }

    private void unbanUser(String banningUsername, String bannedUsername, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("username") != null) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                String query = "DELETE FROM banned_users WHERE banning_user_id = " +
                               "(SELECT id FROM users WHERE username = ?) AND banned_user_id = " +
                               "(SELECT id FROM users WHERE username = ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, banningUsername);
                    preparedStatement.setString(2, bannedUsername);
                    preparedStatement.executeUpdate();
                    try {
                        showHomePage(response, (String) session.getAttribute("username"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User not authenticated");
        }
    }
}

