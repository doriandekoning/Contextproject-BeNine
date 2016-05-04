package com.benine.backend.camera;


/**
 * Interface for communication with remote camera's.
 * @author Bryan
 */

public interface Camera {

  /**
   * Method to send a command to this camera.
   * @param cmd command to send to the camera.
   * @return Response of the command.
   * @throws CameraConnectionException when the request fails.
   */
  String sendCommand(String cmd) throws CameraConnectionException;

String toJSON() throws CameraConnectionException;
 
}
