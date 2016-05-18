package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.CameraFactory;

/**
 * Class to create camera objects.
 * @author Bryan
 *
 */
public class IPCameraFactory extends CameraFactory {


  /**
   * Creates a camera object as specified in camSpec.
   * @param index of the camera in the config file.
   * @return Camera object.
   * @throws InvalidCameraTypeException when specified camera type can not be created.
   */
  public IPCamera createCamera(int index) throws InvalidCameraTypeException {
    Config config = ServerController.getInstance().getConfig();
    String address = config.getValue("camera_" + index + "_address");
    if ( address == null) {
      CameraController.logger.log("Camera: " + index + " has no address specified in the config",
                                                                  LogEvent.Type.CRITICAL);
      throw new InvalidCameraTypeException("Type of camera is not right specified");
    }
    CameraController.logger.log("Create IP camera object", LogEvent.Type.INFO);
    return createIpcamera(address);
  }
  
  /**
   * Creates a IP camera object with the IP as specified.
   * @param ipaddress the address of this camera.
   * @return Camera object.
   */
  public IPCamera createIpcamera(String ipaddress) {
    return new IPCamera(ipaddress);
  }
  

}
