package Service.Request;
import Model.Event;
import Model.Person;
import Model.User;

import java.util.ArrayList;

/**
 * Clears all data from the database (just like the /clear API),
 * and then loads the posted user, person, and event data into the database.
 */
public class LoadRequest {
    private ArrayList<User> users;
    private ArrayList<Person> persons;
    private ArrayList<Event> events;

    /**
     * Requests to load user's data using these values:
     * @param users Array of User objects
     * @param persons Array of Person objects
     * @param events Array of Event objects
     */
    public LoadRequest(ArrayList<User> users,ArrayList<Person> persons,ArrayList<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    // Getters and Setters
    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
