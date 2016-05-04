package com.benine.backend.camera;

/**
 * Exception class for problems with the camera connection.
 * @author Bryan
 *
 */
public class CameraConnectionException extends Exception {
  
  /**
   * Create camera connection exception.
   * @param message to give extra information with exception.
   */
  public CameraConnectionException(String message) {
    super(message);
  }

  /**
   * Serial version ID.
   */
  private static final long serialVersionUID = -6165565835957650746L;

}
