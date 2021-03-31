package Utilites;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSONSerializer {

    // Takes an object and turn it into a JSON string
    public static String serialize(Object object){
        return (new Gson()).toJson(object);
    }

    // Takes a string and turn it into an object
    public static <T> T deserialize(String value, Class<T> returnType) throws JsonParseException{
        try {
            return (new Gson()).fromJson(value, returnType);
        } catch(JsonParseException e){
            e.printStackTrace();
        }
        return null;
    }

    // takes file returns as array of information
    public <T> T parse(File file, Class<T> returnType) throws IOException{
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            return (new Gson()).fromJson(bufferedReader, returnType);
        }
    }
}
