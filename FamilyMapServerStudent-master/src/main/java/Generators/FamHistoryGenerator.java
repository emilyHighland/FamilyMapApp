package Generators;

import Generators.domain.FamHistoryData;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Request.RegisterRequest;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates the user's generational data
 * Each person, excluding the user, must have at least three events (birth, marriage, and death)
 * The user needs to at least have a birth event.
 * This does not mean that a person can ONLY have these events, but they must have AT LEAST these events.
 * (0 generations = the user, 1 generation = the user + parents, 2 generations = the user + parents + grandparents, etc.)
 */
public class FamHistoryGenerator {
    String associatedUsername;
    UUIDGenerator generateID = new UUIDGenerator();
    NameGenerator generateName = new NameGenerator();
    EventGenerator generateEvent = new EventGenerator();
    Random rand = new Random();
    FamHistoryData famData = new FamHistoryData();

    /**
     * default param of 4
     * @param RRequest .
     */
    public User generateData(RegisterRequest RRequest) {
        return generateData(RRequest, 4);
    }

    /**
     * The optional “generations” parameter lets the caller specify the number of generations of ancestors
     *   to be generated, and must be a non-negative integer
     *  (the default is 4, which results in 31 new persons each with associated events)
     *  (0 generations = the user, 1 generation = the user + parents, 2 generations = the user + parents + grandparents, etc.)
     * @param RRequest .
     */
    public User generateData(RegisterRequest RRequest,Integer numGenerations){
        UUIDGenerator generateID = new UUIDGenerator();
        String newID = generateID.UUIDGenerator();

        // create user
        User user = new User(RRequest.getUsername(), RRequest.getPassword(), RRequest.getEmail(),
                RRequest.getFirstName(), RRequest.getLastName(), RRequest.getGender(), newID);

        // person object is the user themself
        Person person = new Person(newID,RRequest.getUsername(),RRequest.getFirstName(), RRequest.getLastName(),
                RRequest.getGender(),null,null,null);

        // generate family history data
        this.famGenerator(person, RRequest.getUsername(), numGenerations);

        // return user to enter into db
        return user;
    }

    // takes a request and generates a Person and Event Array so service class can add to db
    public void famGenerator(Person user, String associatedUsername, int generations){
        this.associatedUsername = associatedUsername;

        // create child birthEvent
        int currentYear = 2021;
        int childBirthYear = currentYear - 8 - rand.nextInt(5);
        Event birthEvent = generateEvent.createEvent(associatedUsername,user.getPersonID(),"birth",childBirthYear);
        famData.addEvent(birthEvent);
//        eventList.add(birthEvent);

        if (generations > 0){
            createOneGeneration(user,generations - 1,currentYear);
        } else if (generations == 0){
            famData.personList.add(user);
//            personList.add(user);
        }
    }

    private void createOneGeneration(Person child,int generations,int currentYear){
        String momID = generateID.UUIDGenerator();
        String dadID = generateID.UUIDGenerator();
        child.setMotherID(momID);
        child.setFatherID(dadID);
        famData.personList.add(child);
//        personList.add(child);

        Person mom = createMom(momID,dadID);
        Person dad = createDad(dadID,momID);

        createGenerationEvents(child,mom,dad,currentYear);

        int genX = 30;
        currentYear = currentYear - genX - rand.nextInt(10);

        if (generations > 0){
            createOneGeneration(mom, generations-1,currentYear);
            createOneGeneration(dad, generations-1,currentYear);
        } else if (generations == 0){
            famData.personList.add(mom);
            famData.personList.add(dad);
//            personList.add(mom);
//            personList.add(dad);
        }
    }

    private Person createMom(String momID,String dadID){
        Person mom = new Person();
        mom.setPersonID(momID);
        mom.setAssociatedUsername(associatedUsername);
        mom.setFirstName(generateName.getName("f"));
        mom.setLastName(generateName.getName("lastname"));
        mom.setGender("f");
        mom.setSpouseID(dadID);
        return mom;
    }

    private Person createDad(String dadID,String momID){
        Person dad = new Person();
        dad.setPersonID(dadID);
        dad.setAssociatedUsername(associatedUsername);
        dad.setFirstName(generateName.getName("m"));
        dad.setLastName(generateName.getName("lastname"));
        dad.setGender("m");
        dad.setSpouseID(momID);
        return dad;
    }

    private void createGenerationEvents(Person Child, Person mom, Person dad, int currentYear){
        int oldestAge = 120;
        int youngestMomAge = 13;
        int oldestMomAge = 50;
        int momBirthYear = currentYear - (2*youngestMomAge) - rand.nextInt(10);
        int dadBirthYear = momBirthYear - rand.nextInt(10);
        int momAge = currentYear - momBirthYear;
        int dadAge = currentYear - dadBirthYear;
        int marriageYear = momBirthYear + youngestMomAge + rand.nextInt(5);
        int momDeathYear = momBirthYear + oldestMomAge + rand.nextInt(30);
        int dadDeathYear = dadBirthYear + oldestMomAge + rand.nextInt(30);

        Event momBirth = generateEvent.createEvent(associatedUsername,mom.getPersonID(),"birth",momBirthYear);
        Event dadBirth = generateEvent.createEvent(associatedUsername,dad.getPersonID(),"birth",dadBirthYear);

        Event momMarriage = generateEvent.createEvent(associatedUsername,mom.getPersonID(),"marriage",marriageYear);
        Event dadMarriage = new Event(generateID.UUIDGenerator(),associatedUsername,dad.getPersonID(),momMarriage.getLatitude(),
                momMarriage.getLongitude(),momMarriage.getCountry(),momMarriage.getCity(),momMarriage.getEventType(),momMarriage.getYear());

        Event momEvent3;

        if (momDeathYear > 2021){
            int deathYear = 2021;
            momEvent3 = generateEvent.createEvent(associatedUsername,mom.getPersonID(),"death",deathYear);
        } else {
            momEvent3 = generateEvent.createEvent(associatedUsername,mom.getPersonID(), "death", momDeathYear);
        }

        Event dadEvent3;
        if (dadDeathYear > 2021){
            int deathYear = 2020;
            dadEvent3 = generateEvent.createEvent(associatedUsername,dad.getPersonID(),"death",deathYear);
        } else {
            dadEvent3 = generateEvent.createEvent(associatedUsername,dad.getPersonID(),"death",dadDeathYear);
        }

        // random event?

        // add generation's events to fam tree
        famData.eventList.add(momBirth);
        famData.eventList.add(dadBirth);
//        eventList.add(momBirth);
//        eventList.add(dadBirth);

        famData.eventList.add(momMarriage);
        famData.eventList.add(dadMarriage);
//        eventList.add(momMarriage);
//        eventList.add(dadMarriage);

        famData.eventList.add(momEvent3);
        famData.eventList.add(dadEvent3);
//        eventList.add(momEvent3);
//        eventList.add(dadEvent3);
    }

    public ArrayList<Person> getPersonList(){
        return famData.getPersonList();
    }

    public ArrayList<Event> getEventList(){
        return famData.getEventList();
    }

}
