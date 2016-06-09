package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.CameraFactory;
import com.benine.backend.camera.InvalidCameraTypeException;

/**
 * Class to create camera objects.
 */

public class IPCameraFactory implements CameraFactory {
  
  private CameraController cameraController;
  
  private Logger logger;
  
  /**
   * Constructs an IPCameraFactory for the specified cameracontroller.
   * @param cameraController this factory is used for.
   */
  public IPCameraFactory(CameraController cameraController) {
    this.cameraController = cameraController;
    this.logger = cameraController.getLogger();
  }


  /**
   * Creates a camera object as specified in camSpec.
   * @param index of the camera in the config file.
   * @return Camera object.
   * @throws InvalidCameraTypeException when specified camera type can not be created.
   */
  public IPCamera createCamera(int index) throws InvalidCameraTypeException {
    Config config = cameraController.getConfig();
    String address = config.getValue("camera_" + index + "_address");
    if ( address == null) {
      logger.log("Camera: " + index 
              + " has no address specified in the config", LogEvent.Type.CRITICAL);
      throw new InvalidCameraTypeException("Type of camera is not right specified");
    }
    logger.log("Create IP camera object", LogEvent.Type.INFO);
    return createIpcamera(address);
  }
  
  /**
   * Creates a IP camera object with the IP as specified.
   * @param ipaddress the address of this camera.
   * @return Camera object.
   */
  public IPCamera createIpcamera(String ipaddress) {
    return new IPCamera(ipaddress, cameraController);
  }
  

}
