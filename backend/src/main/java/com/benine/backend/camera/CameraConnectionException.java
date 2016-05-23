package com.benine.backend.camera;

/**
 * Exception class for problems with the camera connection.
 */
public class CameraConnectionException extends Exception {

  private int camId;

  /**
   * Create camera connection exception.
   * @param message to give extra information with exception.
   * @param camId the id for the camera to which a connection cannot be established.
   */
  public CameraConnectionException(String message, int camId) {
    super(message);
    this.camId = camId;
  }

  /**
   * Serial version ID.
   */
  private static final long serialVersionUID = -6165565835957650746L;


  /**
   * Getter for the camId.
   * @return id of the camera to which a connection cannot be established.
   */
  public int getCamId() {
    return camId;
  }
}
