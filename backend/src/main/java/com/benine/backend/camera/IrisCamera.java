package com.benine.backend.camera;

/**
 * Decorator of a camera with functions to control the iris of the camera.
 *
 */
public interface IrisCamera extends ControllableCamera {
  
  /**
   * Set the control of the iris to on.
   * @param on true for auto iris on.
   * @throws CameraConnectionException when command can not be completed.
   */
  void setAutoIrisOn(boolean on) throws CameraConnectionException, CameraBusyException;

  /**
   * Request if the auto iris is on.
   * @return true if the auto iris is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  boolean isAutoIrisOn() throws CameraConnectionException;
  
  /**
  * Set the iris position.
  * Values between 1 and 99.
  * 1 is closed iris.
  * 99 is open iris.
  * @param pos to set the iris to.
  * @throws CameraConnectionException when command can not be completed.
  */
  void setIrisPosition(int pos) throws CameraConnectionException, CameraBusyException;

  /**
   * Move the iris in the specified direction.
   * Values between 1 and 99 where 50 is stop moving.
   * 1 is iris nearer with max speed
   * 99 is iris further with max speed
   * @param speed value with which speed iris is changing.
   * @throws CameraConnectionException when command can not be completed.
   */
  void moveIris(int speed) throws CameraConnectionException, CameraBusyException;

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws CameraConnectionException when command can not be completed.
   */
  int getIrisPosition() throws CameraConnectionException;

}
