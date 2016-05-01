package com.benine;

import com.benine.ipcameracontrol.Ipcamera;

/**
 * Class to create camera objects.
 * @author Bryan
 *
 */
public class CameraHandler {
  
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
    Ipcamera res = new Ipcamera(ipaddress);

    return res;
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
