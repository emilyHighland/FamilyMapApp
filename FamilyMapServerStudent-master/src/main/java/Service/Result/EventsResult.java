package Service.Result;
import Model.Event;

import java.util.ArrayList;

/**
 * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
 */
public class EventsResult{
    boolean success;
    String message;
    private ArrayList<Event> data;

    public EventsResult(boolean success,String message){
        this.success = success;
        this.message = message;
    }
    /**
     * Values needed to return ALL events for ALL family members of current user:
     * @param success boolean identifier: whether request was successful or not
     * @param data Array of Event objects
     */
    public EventsResult(ArrayList<Event> data, boolean success) {
        this.success = success;
        this.data = data;
    }

    // Getters and Setters
    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
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
