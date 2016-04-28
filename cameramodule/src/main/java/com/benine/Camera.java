package com.benine;

import com.benine.ipcameracontrol.CameraConnectionException;

/**
 * Interface for communication with remote camera's.
 * @author Bryan
 */

public interface Camera {

  /**
  * Move the camera to the specified position.
  * Tilt speed values: 1:SLOW, 2:MID, 3: FAST.
  * Pan speed values: 1 - 30.
  * @param pan in degrees horizontal axis.
  * @param tilt in degrees vertical axis.
  * @param panSpeed integer to specify the speed of the pan movement.
  * @param tiltSpeed integer to specify the speed of the tilt movement.
  * @throws CameraConnectionException when command can not be completed.
  */
  void moveTo(double pan, double tilt, int panSpeed, int tiltSpeed)
                                                    throws CameraConnectionException;

  /**
   * Move the camera with the specified speed.
   * Values between 1 and 99 where 50 is stand still.
   * 99 is max speed in right and up direction.
   * 1 is max speed in left and down direction.
   * @param pan movement direction over horizontal axis.
   * @param tilt movement direction over vertical axis.
   * @throws CameraConnectionException when command can not be completed.
   */
  void move(int pan, int tilt) throws CameraConnectionException;

  /**
   * Get the absolute position of the camera at this moment.
   * @return array with two values 0: Pan, 1: Tilt both in degrees.
   * @throws CameraConnectionException when command can not be completed.
   */
  double[] getPosition() throws CameraConnectionException;

  /**
   *  Get the current zoom position.
   * @return the current zoom position.
   * @throws CameraConnectionException when command can not be completed.
   */
  int getZoomPosition() throws CameraConnectionException;

  /**
   * Zoom to a specified position.
   * @param zpos position to zoom to.
   * @throws CameraConnectionException when command can not be completed.
   */
  void zoomTo(int zpos) throws CameraConnectionException;

  /**
   * Zoom with the specified speed.
   * Value between 1 and 99 where 51 is stop zoom.
   * 99 is max speed in tele direction.
   * 1 is max speed in wide direction.
   * @param dir zoom direction.
   * @throws CameraConnectionException when command can not be completed.
   */
  void zoom(int dir) throws CameraConnectionException;
  
  /**
   * Get the focus position.
   * @return focus position.
   * @throws CameraConnectionException when command can not be completed.
   */
  int getFocusPos() throws CameraConnectionException;

  /**
   * Set the focus position
   * @param pos position of the focus to move to.
   * @throws CameraConnectionException when command can not be completed.
   */
  void setFocusPos(int pos) throws CameraConnectionException;

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
  void setIrisPos(int pos) throws CameraConnectionException;

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws CameraConnectionException when command can not be completed.
   */
  int getIrisPos() throws CameraConnectionException;

  /**
   * Get the URL to the stream of this camera.
   * @return URL in string format.
   * @throws CameraConnectionException when command can not be completed.
   */
  String getStreamLink();

}
