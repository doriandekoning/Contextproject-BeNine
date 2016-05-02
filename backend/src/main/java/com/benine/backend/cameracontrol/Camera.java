package com.benine.backend.cameracontrol;

import java.util.ArrayList;

/**
 * Interface for communication with remote camera's.
 * @author Bryan
 */

public abstract class Camera {
  
  private ArrayList<CameraOperations> operations;

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
  public abstract void moveTo(double pan, double tilt, int panSpeed, int tiltSpeed)
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
  public abstract void move(int pan, int tilt) throws CameraConnectionException;

  /**
   * Get the absolute position of the camera at this moment.
   * @return array with two values 0: Pan, 1: Tilt both in degrees.
   * @throws CameraConnectionException when command can not be completed.
   */
  public abstract double[] getPosition() throws CameraConnectionException;


  /**
   * Get the URL to the stream of this camera.
   * @return URL in string format.
   */
  public abstract String getStreamLink();
  
  /**
   * Get a list of operations possible for this camera.
   * @return ArrayList of CameraOperations
   */
  public ArrayList<CameraOperations> getOperations() {
    return operations;
  }
  
  /**
   * Set the list of camera operations for this camera.
   * @param operations ArrayList of CameraOperations to set.
   */
  public void setOperations(ArrayList<CameraOperations> operations) {
    this.operations = operations;
  }

}
