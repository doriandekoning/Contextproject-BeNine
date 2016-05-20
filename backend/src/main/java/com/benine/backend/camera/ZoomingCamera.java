package com.benine.backend.camera;

/**
 * Decorator of a camera with functions to control the zooming of the camera.
 */
public interface ZoomingCamera extends ControlableCamera {
  
  /**
   * Get the current zoom position.
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

}
