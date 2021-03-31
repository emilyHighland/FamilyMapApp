package Generators;

import Utilites.JSONSerializer;
import Generators.domain.Names;

import java.io.File;
import java.util.Random;

/**
 * reads the specified json names file, returns random nameType requested
 */
public class NameGenerator {

    public String getName(String nameType) {
        JSONSerializer jsonSerializer = new JSONSerializer();
        String filename = null;
        // get correct file
        if (nameType.equals("f")){ filename = "/Users/emilyhighland/FamilyMapServerStudent-master/json/fnames.json"; }
        else if (nameType.equals("m")) { filename = "/Users/emilyhighland/FamilyMapServerStudent-master/json/mnames.json";}
        else if (nameType.equals("lastname")) { filename = "/Users/emilyhighland/FamilyMapServerStudent-master/json/snames.json";}
        assert filename != null;
        File file = new File(filename);

        try {
            Names data = jsonSerializer.parse(file, Names.class);
            // get random name
            Random random = new Random();
            int randomInx = random.nextInt(data.data.size());
            return data.data.get(randomInx);

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.printf("Unable to parse file %s because of exception: %s\n", file, ex.toString());
        }
        return null;
    }

}
