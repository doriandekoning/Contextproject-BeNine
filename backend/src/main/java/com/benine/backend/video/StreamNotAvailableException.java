package com.benine.backend.video;

/**
 * Defines an exception thrown when a stream is not available.
 */
public class StreamNotAvailableException extends Exception {

  /**
   * The id of the camera for which a stream is not available.
   */
  private int camid;

  /**
   * Should be thrown if a stream is not available.
   */
  public StreamNotAvailableException(int camid, String message) {
    super(message);
    this.camid = camid;
  }

  /**
   * Returns the id of the camera for which no stream is available.
   * @return  The id of the camera for which no stream is available.
   */
  public int getCamid() {
    return camid;
  }

  /**
   * Returns a string representation of this exception.
   * @return String with camera id and message.
   */
  public String toString() {
    return "No Stream found for " + camid + " :" + this.getMessage();
  }
}
