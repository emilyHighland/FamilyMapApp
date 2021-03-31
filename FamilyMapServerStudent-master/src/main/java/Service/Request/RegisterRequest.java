package Service.Request;

/**
 * Creates a new user account if no errors: Request property missing or has invalid value, Username already taken by another user.
 */
public class RegisterRequest {
    private String username = null;
    private String password = null;
    private String email = null;
    private String firstName = null;
    private String lastName = null;
    private String gender = null;

    /**
     * User requests to register using these values:
     * @param username non-empty string
     * @param password non-empty string
     * @param email non-empty string
     * @param firstName non-empty string
     * @param lastName non-empty string
     * @param gender char ('f' or 'm')
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email_address) {
        this.email = email_address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
