package Service.Result;

/**
 * Logs in the user and returns an auth token.
 */
public class LoginResult{
    private String authtoken;
    private String username;
    private String personID;
    private boolean success = false;
    private String message = null;

    public LoginResult(String message,boolean success){
        this.message = message;
        this.success = success;
    }
    /**
     * Values needed for result of login request:
     * @param authtoken non-empty auth token string
     * @param username username passed in with request
     * @param personID id of user's Person object
     * @param success boolean identifier: whether request was successful or not
     */
    public LoginResult(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }

    // Getters and Setters
    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
