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
      if(parsedLine!=null) {
        cfg.addAttribute(parsedLine[0], parsedLine[1]);
      }
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
    Pattern pattern = Pattern.compile("([\\s*+])");
    Pattern wellFormed = Pattern.compile("\\w*=\\w*");
    Matcher matcher = pattern.matcher(line.split("#")[0]);
    String whiteSpaceRemoved = matcher.replaceAll("");
    // If the line only contains a comment return null
    if(whiteSpaceRemoved.equals("")) {
      return null;
      // Check if line is a valid config line
    } else if (wellFormed.matcher(whiteSpaceRemoved).matches()) {
      // Check if the line contains data or is just a comment
        String[] stringSplit = whiteSpaceRemoved.split("=");
        return stringSplit;
    } else {
      throw new InvalidConfigFileException("Malformed  line: " + whiteSpaceRemoved);
    }


  }

  /**
   * Exception thrown when config file is not valid.
   */
  public static class InvalidConfigFileException extends Exception {
    public InvalidConfigFileException(String reason) {
      super(reason);
    }
  }
}
