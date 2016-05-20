package com.benine.backend.video;

/**
 * Defines an exception thrown when a stream is not available.
 */
public class StreamNotAvailableException extends Exception {

  /**
   * Serial Version.
   */
  private static final long serialVersionUID = -8728176073385756365L;
  
  /**
   * The id of the camera for which a stream is not available.
   */
  private int camid;

  /**
   * Should be thrown if a stream is not available.
   * @param camid camera for which the stream is not available
   * @param message reason for exception.
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
