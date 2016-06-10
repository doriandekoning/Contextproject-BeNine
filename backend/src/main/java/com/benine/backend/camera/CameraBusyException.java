package com.benine.backend.camera;

/**
 * Exception class used to throw when camera is busy but move methods are called.
 */
public class CameraBusyException extends Exception {

  private int camId = -1;

  /**
   * Create camera busy exception.
   * @param message to give extra information with exception.
   * @param camId Camera that causes this exception.
   */
  public CameraBusyException(String message, int camId) {
    super(message);
    this.camId = camId;
  }


  public int getCamId() {
    return camId;
  }
  /**
   * Serial version ID.
   */
  private static final long serialVersionUID = -6165565835957650746L;

}
