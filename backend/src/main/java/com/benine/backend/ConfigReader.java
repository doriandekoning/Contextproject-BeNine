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
   * @throws Exception when config can not be read.
   */
  public static Config readConfig(String location) throws Exception {
    Config cfg = new Config();
    BufferedReader br = new BufferedReader(new FileReader(location));
    String line;
    while ((line = br.readLine()) != null) {
      // Handle each line
      String[] parsedLine = parseLine(line);
      if (parsedLine != null) {
        cfg.addAttribute(parsedLine[0], parsedLine[1]);
      }
    }
    br.close();
    return cfg;
  }

  /**
   * parses a line and returns the attribute name and value in a string array.
   * @param line the line to be parsed.
   * @return Null if line is a comment or string array with attribute value and key.
   * @throws InvalidConfigFileException if the config file is not valid
   */
  //TODO add support .s in value
  private static String[] parseLine(String line) throws InvalidConfigFileException {
    // Remove comments (everything after a #)
    Pattern pattern = Pattern.compile("([\\s*+])");
    //All config files that have a word as key are accepted. The value can contain all characters.
    Pattern wellFormed = Pattern.compile("\\w*=.{1,}");
    Matcher matcher = pattern.matcher(line.split("#")[0]);
    String whiteSpaceRemoved = matcher.replaceAll("");
    // If the line only contains a comment return null
    if (whiteSpaceRemoved.equals("")) {
      return null;
      // Check if line is a valid config line
    } else if (wellFormed.matcher(whiteSpaceRemoved).matches()) {
      // Check if the line contains data or is just a comment
      return whiteSpaceRemoved.split("=");
    } else {
      throw new InvalidConfigFileException("Malformed  line: " + whiteSpaceRemoved);
    }


  }

  /**
   * Exception thrown when config file is not valid.
   */
  public static class InvalidConfigFileException extends Exception {
    
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Exception for invalid config file.
     * @param reason for the file is invalid.
     */
    public InvalidConfigFileException(String reason) {
      super(reason);
    }
  }
}
