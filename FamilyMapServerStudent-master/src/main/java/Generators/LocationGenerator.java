package Generators;

import Utilites.JSONSerializer;
import Model.Location;
import Generators.domain.LocationData;

import java.io.File;
import java.util.Random;

/**
 * reads locations.json in json directory, picks random location
 * returns random generated location
 */
public class LocationGenerator {

    public Location getLocation() {
        JSONSerializer jsonSerializer = new JSONSerializer();
        File file = new File("/Users/emilyhighland/FamilyMapServerStudent-master/json/locations.json");

        try {
            LocationData locData = jsonSerializer.parse(file, LocationData.class);
            Random random = new Random();
            int randomInx = random.nextInt(locData.data.length);
            return locData.getLocations(randomInx);

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.printf("Unable to parse file %s because of exception: %s\n", file, ex.toString());
        }
        return null;
    }
}
