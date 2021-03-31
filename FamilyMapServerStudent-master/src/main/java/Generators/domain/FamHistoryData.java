package Generators.domain;

import Model.Event;
import Model.Person;

import java.util.ArrayList;

public class FamHistoryData {
    public ArrayList<Person> personList = new ArrayList<>();
    public ArrayList<Event> eventList = new ArrayList<>();

    public void addPerson(Person person) {
//        this.personList = personList;
        personList.add(person);
    }

    public void addEvent(Event event) {
//        this.eventList = eventList;
        eventList.add(event);
    }

    public ArrayList<Person> getPersonList() { return personList; }

    public ArrayList<Event> getEventList() { return eventList; }
}
