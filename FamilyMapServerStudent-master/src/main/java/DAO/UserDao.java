package DAO;
import Model.User;

import java.sql.*;

/**
 * Inserts and retrieves User object data to and from the database.
 */
public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert new data into the users table.
     * @param user data of User object to load to database
     * @throws DataAccessException error occurred while add to the database
     */
    public void insert(User user)throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO users (username, password, email, firstName, " +
                "lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into users");
        }
    }

    /**
     * Retrieve specific information from the users table in the database.
     * @param username specific name for each user, associated with user specific information
     * @return User object with associated data from the database
     * @throws DataAccessException error occurred while locating user
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Clear all information from the users table.
     * @throws DataAccessException error occurred while clearing table
     */
    public void clearUsers() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM users;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing users table");
        }
    }
}
