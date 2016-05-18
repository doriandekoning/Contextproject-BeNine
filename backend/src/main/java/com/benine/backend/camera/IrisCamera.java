package com.benine.backend.camera;

/**
 * Decorator of a camera with functions to control the iris of the camera.
 * @author Bryan
 *
 */
public interface IrisCamera extends ControlableCamera {
  
  /**
   * Set the control of the iris to on.
   * @param on true for auto iris on.
   * @throws CameraConnectionException when command can not be completed.
   */
  void setAutoIrisOn(boolean on) throws CameraConnectionException;

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
  void setIrisPosition(int pos) throws CameraConnectionException;

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws CameraConnectionException when command can not be completed.
   */
  int getIrisPosition() throws CameraConnectionException;

}
