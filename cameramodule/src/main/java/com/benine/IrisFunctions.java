package com.benine;

import com.benine.ipcameracontrol.IpcameraConnectionException;

/**
 * Interface for camera iris functions.
 * @author Bryan
 *
 */
public interface IrisFunctions {
  
  /**
   * Set the control of the iris to on.
   * @param on true for auto iris on.
   * @throws CameraConnectionException when command can not be completed.
   */
  void setAutoIrisOn(boolean on) throws IpcameraConnectionException;

  /**
   * Request if the auto iris is on.
   * @return true if the auto iris is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  boolean isAutoIrisOn() throws IpcameraConnectionException;

  /**
   * Set the iris position.
   * Values between 1 and 99.
   * 1 is closed iris.
   * 99 is open iris.
   * @param pos to set the iris to.
   * @throws CameraConnectionException when command can not be completed.
   */
  void setIrisPos(int pos) throws IpcameraConnectionException;

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws CameraConnectionException when command can not be completed.
   */
  int getIrisPos() throws IpcameraConnectionException;

}
