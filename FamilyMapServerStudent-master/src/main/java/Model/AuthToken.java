package Model;

/**
 * Generates a unique “authorization token” string for the user.
 * Subsequent requests sent from the user should include the auth token so your server can determine which user is making the request.
 */
public class AuthToken {
    private String associatedUsername;
    private String authtoken;

    public AuthToken(){}
    /**
     * Unique “authorization token” string for the associated username.
     * @param username unique username
     * @param authtoken unique token associated with current user
     */
    public AuthToken(String username, String authtoken) {
        this.associatedUsername = username;
        this.authtoken = authtoken;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return getAssociatedUsername().equals(authToken1.getAssociatedUsername()) &&
                getAuthtoken().equals(authToken1.getAuthtoken());
    }
}
