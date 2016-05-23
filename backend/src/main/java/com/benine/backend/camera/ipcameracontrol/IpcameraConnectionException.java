package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.camera.CameraConnectionException;

/**
 * Exception class for problems with the IP connection.
 */
public class IpcameraConnectionException extends CameraConnectionException {
  
  /**
   * Create Ip connection exception.
   * @param message to give extra information with exception.
   * @param camId Camera that causes this exception.
   */
  public IpcameraConnectionException(String message, int camId) {
    super(message, camId);
  }

  /**
   * Serial version ID.
   */
  private static final long serialVersionUID = -6165565835957650746L;

}
