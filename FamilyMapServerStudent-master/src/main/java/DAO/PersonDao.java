package DAO;
import Model.Person;

import java.sql.*;
import java.util.ArrayList;

/**
 * Inserts and retrieves Person object data to and from the database.
 */
public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn){
        this.conn = conn;
    }

    /**
     * Insert new data into the persons table.
     * @param person object with associated data to load into the database.
     * @throws DataAccessException error occurred while add to the database
     */
    public void insert(Person person) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO persons (personID, associatedUsername, firstName, lastName, " +
                "gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1,person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into persons");
        }
    }

    /**
     * Retrieve specific information from the persons table in the database.
     * @param personID specific name for each user, associated with user specific information
     * @return Person object with associated data from the database
     * @throws DataAccessException error occurred while locating personID
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM persons WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
     * Retrieves all persons with associated username
     * @param associatedUsername the specific username the person belongs to
     * @return list of persons from db with associated username
     * @throws DataAccessException error retrieving persons
     */
    public ArrayList<Person> findPersonsWhere_(String associatedUsername) throws DataAccessException {
        Person person;
        ArrayList<Person> personsArray = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM persons WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while(rs.next()) {
                // retrieve one person
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                // add each person to the list
                personsArray.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding list of persons");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return personsArray;
    }

    /**
     * Deletes persons from db associated with a specific username
     * @param associatedUsername name of the associated user
     * @throws DataAccessException error deleting persons with associated username
     */
    public void deletePersonsWhere_(String associatedUsername) throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM persons WHERE associatedUsername = '" + associatedUsername + "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while deleting " + associatedUsername + " from persons");
        }
    }

    /**
     * Clear all information from the persons table.
     * @throws DataAccessException error occurred while clearing table
     */
    public void clearPersons() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM persons;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing persons table");
        }
    }
}
