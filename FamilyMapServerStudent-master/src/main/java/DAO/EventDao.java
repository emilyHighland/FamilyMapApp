package DAO;
import Model.Event;

import java.sql.*;
import java.util.ArrayList;

/**
 * Inserts and retrieves Event object data to and from the database.
 */
public class EventDao {

    private final Connection conn;

    public EventDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert new data into the Events table.
     * @param event a specific event with associating details
     * @throws DataAccessException error occurred while add to the database
     */
    public void insert(Event event) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO events (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into events");
        }
    }

    /**
     * Finds details of a related specific event.
     * @param eventID a specific event
     * @return Event object with details of specific event requested
     * @throws DataAccessException error occurred while locating event
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Finds all events with the associated username
     * @param associatedUsername the specific username the event belongs to
     * @return array of events
     * @throws DataAccessException error retrieving events
     */
    public ArrayList<Event> findEventsWhere_(String associatedUsername) throws DataAccessException {
        Event event;
        ArrayList<Event> eventsArray = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                // retrieve one event
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                // add each event to eventsArray
                eventsArray.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding array of events");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return eventsArray;
    }

    /**
     * Deletes information from db associated with a specific username
     * @param associatedUsername name of user
     * @throws DataAccessException error while deleting events with associated username
     */
    public void deleteEventsWhere_(String associatedUsername) throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM events WHERE associatedUsername = '" + associatedUsername + "';";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while deleting " + associatedUsername + " from events");
        }
    }

    /**
     * Clear all information from the Events table.
     * @throws DataAccessException error occurred while clearing table
     */
    public void clearEvents() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM events;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing Events table");
        }
    }
}
