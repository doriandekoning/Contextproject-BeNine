package com.benine.backend.cameracontrol;

/**
 * Interface for Focus functions of camera's.
 * @author Bryan
 */
public interface FocusFunctions {
  

  /**
   * Get the focus position.
   * @return focus position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public abstract int getFocusPos() throws CameraConnectionException;

  /**
   * Set the focus position
   * @param pos position of the focus to move to.
   * @throws CameraConnectionException when command can not be completed.
   */
  public abstract void setFocusPos(int pos) throws CameraConnectionException;

  /**
   * Move the focus in the specified direction.
   * Values between 1 and 99 where 50 is stop focusing.
   * 1 is focus nearer with max speed
   * 99 is focus further with max speed
   * @param speed value with which speed is focusing.
   * @throws CameraConnectionException when command can not be completed.
   */
  public abstract void moveFocus(int speed) throws CameraConnectionException;

  /**
   * Turn auto focus on or off.
   * @param on true for auto focus on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public abstract void setAutoFocusOn(boolean on) throws CameraConnectionException;

  /**
   * Request if the auto focus is on.
   * @return true if auto focus is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public abstract boolean isAutoFocusOn() throws CameraConnectionException;

 

}
