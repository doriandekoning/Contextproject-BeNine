package com.benine.backend.cameracontrol;

/**
 * Interface for communication with remote camera's.
 * @author Bryan
 */

public abstract class Camera {
  
  private IrisFunctions iris;
  
  private ZoomFunctions zoom;
  
  private FocusFunctions focus;

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
   * @throws CameraConnectionException when command can not be completed.
   */
  public abstract String getStreamLink();
  
  /**
   * Get the functions to control the iris of this camera.
   * @return Iris fucntions object.
   */
  public IrisFunctions getIris() {
    return iris;
  }
  
  /**
   * Set the iris functions of this camera.
   * @param iris function object.
   */
  public void setIris(IrisFunctions iris) {
    this.iris = iris;
  }
  
  /**
   * Get the zoom functions.
   * @return zoom functions object.
   */
  public ZoomFunctions getZoom() {
    return zoom;
  }
  
  /**
   * Set the zoom functions to control the zooming of this camera.
   * @param zoom function object.
   */
  public void setZoom(ZoomFunctions zoom) {
    this.zoom = zoom;
  }
  
  /**
   * Get the focus functions.
   * @return focus functions object.
   */
  public FocusFunctions getFocus() {
    return focus;
  }
  
  /**
   * Set the focus functions object.
   * @param focus functions object.
   */
  public void setFocus(FocusFunctions focus) {
    this.focus = focus;
  }

}
