package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Uzivatel;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    private final Connection dbConnection;

    public UserService() {
        this.dbConnection = DatabaseService.getInstance().getConnection();
    }

    /**
     * Checks if a user exists with the given username or email.
     * @param username The username to check.
     * @param email The email to check.
     * @return True if a user exists, false otherwise.
     */
    public boolean userExists(String username, String email) {
        // Basic validation to prevent SQL injection, although PreparedStatement is the main protection.
        if (username == null || username.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM Uzivatel WHERE username = ? OR email = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Saves a new user to the database with a hashed password.
     * @param user The Uzivatel object to save.
     * @return True if the user was saved successfully, false otherwise.
     */
    public boolean saveUser(Uzivatel user) {
        if (user == null) {
            return false;
        }
        // Hash the password before saving
        String hashedPassword = BCrypt.hashpw(user.getPasswd(), BCrypt.gensalt());

        String sql = "INSERT INTO Uzivatel(name, username, email, passwd) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, hashedPassword);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
