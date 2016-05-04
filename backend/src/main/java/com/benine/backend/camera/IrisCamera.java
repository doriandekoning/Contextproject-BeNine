package com.benine.backend.camera;

import com.benine.backend.camera.ipcameracontrol.IpcameraConnectionException;

/**
 * Decorator of a camera with functions to control the iris of the camera.
 * @author Bryan
 *
 */
public interface IrisCamera extends Camera {
  
  /**
   * Set the control of the iris to on.
   * @param on true for auto iris on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setAutoIrisOn(boolean on) throws CameraConnectionException;

  /**
   * Request if the auto iris is on.
   * @return true if the auto iris is on.
   * @throws IpcameraConnectionException when command can not be completed.
   */
  public boolean isAutoIrisOn() throws CameraConnectionException;
  
  /**
  * Set the iris position.
  * Values between 1 and 99.
  * 1 is closed iris.
  * 99 is open iris.
  * @param pos to set the iris to.
  * @throws CameraConnectionException when command can not be completed.
  */
  public void setIrisPos(int pos) throws CameraConnectionException;

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getIrisPos() throws CameraConnectionException;

}
