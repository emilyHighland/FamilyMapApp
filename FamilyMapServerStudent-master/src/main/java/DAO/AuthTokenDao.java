package DAO;

import Model.AuthToken;
import java.sql.*;

/**
 * Inserts and retrieves AuthToken object data to and from the database.
 */
public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert new data into the authTokens table.
     * @param newToken object with unique auth token and associated username
     * @throws DataAccessException error occurred while adding to the database
     */
    public void insert(AuthToken newToken) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO authTokens (authToken, associatedUsername) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, newToken.getAuthtoken());
            stmt.setString(2, newToken.getAssociatedUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into authTokens");
        }
    }

    /**
     * Retrieve information from the authTokens table.
     * @param authToken
     * @return AuthToken object with unique auth token and associated username
     * @throws DataAccessException error occurred while locating username
     */
    public AuthToken find(String authToken) throws DataAccessException {
        AuthToken authTokenObj;
        ResultSet rs = null;
        String sql = "SELECT * FROM authTokens WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authTokenObj = new AuthToken(rs.getString("associatedUsername"),
                        rs.getString("authToken"));
                return authTokenObj;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting authToken");
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
     * Clear all information from the authTokens table.
     * @throws DAO.DataAccessException error occurred while clearing table
     */
    public void clearAuthTokens() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM authTokens;";
            stmt.executeUpdate(sql);
        }catch(SQLException e){
            throw new DataAccessException("SQL Error encountered while clearing authTokens table");
        }
    }

}
