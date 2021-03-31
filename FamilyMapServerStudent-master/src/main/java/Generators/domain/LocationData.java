package Generators.domain;

import Model.Location;

public class LocationData {
    public Location[] data;

    public LocationData(Location[] locData){
        data = locData;
    }

    public Location getLocations(int indx){
        return data[indx];
    }
}
