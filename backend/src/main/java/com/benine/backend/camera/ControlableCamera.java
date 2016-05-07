package com.benine.backend.camera;

/**
 * Created by dorian on 5-5-16.
 */
public interface ControlableCamera extends Camera {
  /**
   * Method to send a command to this camera.
   * @param cmd command to send to the camera.
   * @return Response of the command.
   * @throws CameraConnectionException when the request fails.
   */
  String sendCommand(String cmd) throws CameraConnectionException;
}