package com.benine.backend.video;

/**
 * Defines an exception thrown when a stream is not available.
 */
public class StreamNotAvailableException extends Exception {

  private int camid;

  /**
   * Should be thrown if a stream is not available.
   * @param camid The id of the camere for which the stream is not available.
   */
  public StreamNotAvailableException(int camid) {
    this.camid = camid;
  }

  /**
   * Returns the id of the camera for which no stream is available.
   * @return  The id of the camera for which no stream is available.
   */
  public int getCamid() {
    return camid;
  }
}
