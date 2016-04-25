package com.benine.backend;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  public static Config readConfig(String location) throws Exception {
    Config cfg = new Config();
    BufferedReader br = new BufferedReader(new FileReader(location));
    String line;
    while ((line = br.readLine()) != null) {
      // Handle each line
      String[] parsedLine = parseLine(line);
      cfg.addAttribute(parsedLine[0], parsedLine[1]);
    }
    return cfg;
  }

  /**
   * parses a line and returns the attribute name and value in a string array.
   * @param line the line to be parsed.
   * @return Null if line is a comment or string array with attribute value and key.
   * @throws InvalidConfigFileException if the config file is not valid
   */
  private static String[] parseLine(String line) throws InvalidConfigFileException {
    // Remove comments (everything after a #)
    Pattern pattern = Pattern.compile("(#.*)|([\\s*+])");
    Matcher matcher = pattern.matcher(line);
    line = matcher.replaceAll("");
    // Check if line is a valid config line
    pattern = Pattern.compile("(.*=.*)");
    matcher = pattern.matcher(line);
    // Line is valid
    if (matcher.matches()) {
      String[] stringSplit = line.split("=");
      System.out.println("Stringsplit length:" + stringSplit.length + stringSplit[0]);
      return stringSplit;
    } else {
      // TODO specify bad weather behaviour
      return null;
    }

  }

  /**
   * Exception thrown when config file is not valid.
   */
  private static class InvalidConfigFileException extends Exception {
    public InvalidConfigFileException(String reason) {
      super(reason);
    }
  }
}
