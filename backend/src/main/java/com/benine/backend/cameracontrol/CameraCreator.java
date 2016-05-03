package com.benine.backend.cameracontrol;

import com.benine.backend.cameracontrol.ipcameracontrol.FocussingIPCamera;
import com.benine.backend.cameracontrol.ipcameracontrol.IPCamera;
import com.benine.backend.cameracontrol.ipcameracontrol.IrisIPCamera;
import com.benine.backend.cameracontrol.ipcameracontrol.MovingIPCamera;
import com.benine.backend.cameracontrol.ipcameracontrol.ZoomingIPCamera;

/**
 * Class to create camera objects.
 * @author Bryan
 *
 */
public class CameraCreator {
  
  /**
   * Constructor of the camera handler.
   */
  public CameraCreator(){
    
  }
  
  /**
   * Creates a camera object as specified in camSpec.
   * @param camSpec specification of the camera 0: type, 1: additional info.
   * @return Camera object.
   * @throws InvalidCameraTypeException when specified camera type can not be created.
   */
  public Camera createCamera(String[] camSpec) throws InvalidCameraTypeException {
    switch (camSpec[0]) {
      case "ipcamera" : return createIpcamera(camSpec[1]);
      default: throw new InvalidCameraTypeException("Type of camera is not right specified");
    }
  }
  
  /**
   * Creates a IP camera object with the IP as specified.
   * @param ipaddress the address of this camera.
   * @return Camera object.
   */
  public Camera createIpcamera(String ipaddress) {
    Camera ipcamera = new IPCamera(ipaddress);
    Camera zoomcam = new ZoomingIPCamera(ipcamera);
    Camera focuscam = new FocussingIPCamera(zoomcam);
    Camera iriscam = new IrisIPCamera(focuscam);
    return new MovingIPCamera(iriscam);
  }
  
  /**
   * Exception thrown when camera object type is not available.
   */
  public static class InvalidCameraTypeException extends Exception {
    
    /**
     * Serial version.
     */
    private static final long serialVersionUID = 10863715123938677L;

    public InvalidCameraTypeException(String reason) {
      super(reason);
    }
  }

}
