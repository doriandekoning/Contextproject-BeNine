package com.benine.backend.camera;

/**
 * Decorator of a camera with functions to control the Focussing of the camera.
 * @author Bryan
 */
public interface FocussingCamera extends ControlableCamera {
  
  /**
   * Get the focus position.
   * @return focus position.
   * @throws CameraConnectionException when command can not be completed.
   */
  int getFocusPosition() throws CameraConnectionException;

  /**
   * Set the focus position
   * @param pos position of the focus to move to.
   * @throws CameraConnectionException when command can not be completed.
   */
  void setFocusPosition(int pos) throws CameraConnectionException;

  /**
   * Move the focus in the specified direction.
   * Values between 1 and 99 where 50 is stop focusing.
   * 1 is focus nearer with max speed
   * 99 is focus further with max speed
   * @param speed value with which speed is focusing.
   * @throws CameraConnectionException when command can not be completed.
   */
  void moveFocus(int speed) throws CameraConnectionException;

  /**
   * Turn auto focus on or off.
   * @param on true for auto focus on.
   * @throws CameraConnectionException when command can not be completed.
   */
  void setAutoFocusOn(boolean on) throws CameraConnectionException;
  
  /**
   * Request if the auto focus is on.
   * @return true if auto focus is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  boolean isAutoFocusOn() throws CameraConnectionException;

}
