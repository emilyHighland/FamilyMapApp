package Service.Result;

/**
 * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
 */
public class RegisterResult{
    private String authtoken = null;
    private String username = null;
    private String personID = null;
    private boolean success = false;
    private String message = null;

    public RegisterResult(boolean success,String message){
        this.success = success;
        this.message = message;
    }
    /**
     * Values needed to register user:
     * @param authtoken non-empty auth token string
     * @param username username passed in with request
     * @param personID id of the user's generated Person object
     * @param success boolean identifier: whether request was successful or not
     */
    public RegisterResult(String authtoken, String username, String personID, boolean success) {
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
