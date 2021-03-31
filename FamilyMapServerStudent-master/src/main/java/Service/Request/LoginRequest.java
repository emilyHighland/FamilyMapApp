package Service.Request;

/**
 * If username and password are valid, logs in user and returns an auth token.
 */
public class LoginRequest {
    private String username = null;
    private String password = null;

    /**
     * User requests login using these values:
     * @param username non-empty string to see if valid username
     * @param password non-empty string to see if valid password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
