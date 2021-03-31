package Service.Result;

/**
 * Returns the single Event object with the specified ID.
 */
public class EventResult {
    private String associatedUsername = null;
    private String eventID = null;
    private String personID = null;
    private float latitude;
    private float longitude;
    private String country = null;
    private String city = null;
    private String eventType = null;
    private int year;
    boolean success;
    String message;

    public EventResult(boolean success,String message){
        this.success = success;
        this.message = message;
    }
    /**
     * Values needed to return the single Event specified of current user:
     * @param associatedUsername username tied to the event
     * @param eventID specific event's unique id
     * @param personID person id associated with the even
     * @param latitude map coordinates of where the event happened
     * @param longitude map coordinates of where the event happened
     * @param country name of country where event took place
     * @param city name of city where event took place
     * @param eventType type of event: (birth, baptism, etc.)
     * @param year year the event occurred
     * @param success boolean identifier: whether request was successful or not
     */
    public EventResult(String associatedUsername, String eventID, String personID, float latitude,
                       float longitude, String country, String city, String eventType, int year, boolean success) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.success = success;
    }

    // Getters and Setters
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
