package Generators;

import Model.Event;
import Model.Location;

import java.util.Random;

/**
 * Each person, excluding the user, must have at least three events (birth, marriage, and death)
 * The user needs to at least have a birth event.
 * This does not mean that a person can ONLY have these events, but they must have AT LEAST these events.
 * Parents should be born a reasonable number of years before their children (at least 13 years), get married at a reasonable age, and not die before their child is born.
 * Also, females should not give birth at over 50 years old. Birth events need be first, and death events need to be last. No one should die at over 120 years old.
 * Each person in a couple has their own marriage event, but their two marriage events need to have the same year and location. Event locations may be randomly selected, or you can try to make them more realistic (e.g., many people live their lives in a relatively small geographical area).
 * Your code needs to account for any possible event type, even if you yourself do not generate those events. For example you might only generate events for Birth, Marriage, and Death.
 * You should be able to also handle event types such as Baptism, Christening, or any other data that may be loaded into your server.
 */
public class EventGenerator {
    UUIDGenerator idGenerator = new UUIDGenerator();
    LocationGenerator generateLocation = new LocationGenerator();
    int year;

    public Event createEvent(String associatedUsername, String personID, String eventType,int year){
        // get random location from location generator
        Location location = generateLocation.getLocation();
        return new Event(idGenerator.UUIDGenerator(),associatedUsername,personID,location.getLatitude(),location.getLongitude(),
                location.getCountry(),location.getCity(),eventType,year);

    }

    public Event createRandomEvent(String username,String personID,int age,int currentYear){
        // get random location from location generator
        Location location = generateLocation.getLocation();

        String eventType = getRandomEventType(age,currentYear);

        return new Event(idGenerator.UUIDGenerator(),username,personID,location.getLatitude(),location.getLongitude(),
                location.getCountry(),location.getCity(),eventType,year);

    }

    private String getRandomEventType(int age,int currentYear){
        Random rand = new Random();
        int choice = rand.nextInt(5);

        switch (choice) {
            case 1:
                year = currentYear - age + 1;
                return "christening";
            case 2:
                year = currentYear - age + 8;
                return "baptism";
            case 3:
                year = currentYear - age + 13;
                return "hiked_kilimanjaro";
        }
        return "won_the_lottery";
    }

}
