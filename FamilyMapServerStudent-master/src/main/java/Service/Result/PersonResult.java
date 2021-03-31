package Service.Result;

/**
 * Returns the single Person object with the specified ID.
 */
public class PersonResult{
    private String associatedUsername = null;
    private String personID = null;
    private String firstName = null;
    private String lastName = null;
    private String gender = null;
    private String fatherID = null;
    private String motherID = null;
    private String spouseID = null;
    boolean success;
    String message;

    public PersonResult(boolean success,String message){
        this.success = success;
        this.message = message;
    }
    /**
     * Values needed to return the data of the current user:
     * @param associatedUsername Name of user account
     * @param personID person's unique id
     * @param firstName person's first name
     * @param lastName person's last name
     * @param gender person's gender ('m' or 'f')
     * @param fatherID id of person's father [OPTIONAL, can be missing]
     * @param motherID id of person's mother [OPTIONAL, can be missing]
     * @param spouseID id of person's spouse [OPTIONAL, can be missing]
     * @param success boolean identifier: whether request was successful or not
     */
    public PersonResult(String associatedUsername, String personID, String firstName, String lastName,
                        String gender, String fatherID, String motherID, String spouseID, boolean success) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = success;
    }

    // Getters and Setters
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
