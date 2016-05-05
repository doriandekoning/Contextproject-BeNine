package com.benine.backend;

import java.util.jar.Attributes;

/**
* Class that is used to store config settings.
* This class basically an interface to an Attributes object but offers a layer of protection.
* @author Dorian
*/
public class Config {

  private Attributes attributes;

  /**
   * Create new Config object.
   */
  public Config() {
    attributes = new Attributes();
  }

  /**
   * Add (attribute, value) pair to this config.
   * @param attribute The name of the attribute.
   * @param value The value of the attribute.
   */
  public void addAttribute(String attribute, String value) {
    attributes.put(new Attributes.Name(attribute), value);
  }

  /**
   * Get the value associated with this attribute from the config.
   * @param attributeName The name of the attribute to get the value for
   * @return The value associated with this value, empty string if no value is found.
   */
  public String getValue(String attributeName) {
    try {
      return attributes.getValue(new Attributes.Name(attributeName));
    } catch (IllegalArgumentException e) {
      //TODO log that the config value is not found
      return "";
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((attributes == null) ? 0 : attributes.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Config) {
      Config that = (Config) other;
      return this.attributes.equals(that.attributes);
    } else {
      return false;
    }
  }
}
