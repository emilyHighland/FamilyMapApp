package com.example.myfamilymap;

import Model.AuthToken;
import Model.FamHistoryData;
import Model.Person;
import Model.User;
import Result.ClearResult;
import Result.EventResult;
import Result.EventsResult;
import Result.FillResult;
import Result.LoadResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.PersonsResult;
import Result.RegisterResult;

public class DataCache {
    private static DataCache instance;
    private LoginResult loginResult;
    private RegisterResult registerResult;
    private PersonsResult personsResult;
    private EventsResult eventsResult;
    private ClearResult clearResult;
    private LoadResult loadResult;
    private FillResult fillResult;
    private PersonResult personResult;
    private EventResult eventResult;

    // has no authToken but personID
    private User currentUser;
    private FamHistoryData famTree;

    boolean dataRetrieved = false;



    public static DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache() { }


    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    public RegisterResult getRegisterResult() {
        return registerResult;
    }

    public void setRegisterResult(RegisterResult registerResult) {
        this.registerResult = registerResult;
    }

    public PersonsResult getPersonsResult() {
        return personsResult;
    }

    public void setPersonsResult(PersonsResult personsResult) {
        this.personsResult = personsResult;
    }

    public EventsResult getEventsResult() {
        return eventsResult;
    }

    public void setEventsResult(EventsResult eventsResult) {
        this.eventsResult = eventsResult;
    }

    public LoadResult getLoadResult() {
        return loadResult;
    }

    public void setLoadResult(LoadResult loadResult) {
        this.loadResult = loadResult;
    }


    public FamHistoryData getFamTree() {
        return famTree;
    }

    public void setFamTree(FamHistoryData famTree) {
        this.famTree = famTree;
    }
}
