package Service.Result;

import Model.Person;

import java.util.ArrayList;

/**
 * Returns ALL family members of the current user. The current user is determined from the provided auth token.
 */
public class PersonsResult{
    private ArrayList<Person> data;
    boolean success;
    String message;

    public PersonsResult(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    /**
     * Values needed to return ALL family members of current user:
     * @param data Array of Person objects
     * @param success boolean identifier: whether request was successful or not
     */
    public PersonsResult(ArrayList<Person> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    // Getters and Setters
    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
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
