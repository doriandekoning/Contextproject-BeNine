package com.benine.backend;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class used for reading configuration details from a .config file and creates a new config object.
 * @author Dorian
 */
public class ConfigReader {
    /**
     * Reads a config file from a string and returns an new config object.
     * @param location the location of the file
     * @return A new config object containing the attribute, value pares from the config file.
     */
    public static Config readConfig(String location) throws IOException {
       BufferedReader br = new BufferedReader(new FileReader(location));
        String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        return null;
    }
}
