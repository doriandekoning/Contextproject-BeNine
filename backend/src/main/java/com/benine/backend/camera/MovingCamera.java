package com.benine.backend.camera;

/**
 * Decorator of a camera with functions to control the movements of the camera.
 * @author Bryan
 */
public interface MovingCamera extends ControlableCamera {
  
  /**
   * Move the camera to the specified position.
   * Tilt speed values: 1:SLOW, 2:MID, 3: FAST.
   * Pan speed values: 1 - 30.
   * @param pos position to move to.
   * @param panSpeed integer to specify the speed of the pan movement.
   * @param tiltSpeed integer to specify the speed of the tilt movement.
   * @throws CameraConnectionException when command can not be completed.
   */
  void moveTo(Position pos, int panSpeed, int tiltSpeed)
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
  Position getPosition() throws CameraConnectionException;

}
