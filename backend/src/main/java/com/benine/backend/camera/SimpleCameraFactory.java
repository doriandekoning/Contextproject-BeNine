package com.benine.backend.camera;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;

/**
 * Factory for the creation of a simple camera object.
 *
 */
public class SimpleCameraFactory implements CameraFactory {
  
  CameraController cameraController;
  
  /**
   * Constructs a simple camera factory for the specified cameraController.
   * @param cameraController to create a simpleCameraFactory for.
   */
  public SimpleCameraFactory(CameraController cameraController) {
    this.cameraController = cameraController;
  }

  @Override
  public Camera createCamera(int index) throws InvalidCameraTypeException {
    Config config = cameraController.getConfig();
    String address = config.getValue("camera_" + index + "_address");
    String mACaddress = config.getValue("camera_" + index + "_macaddress");
    if (address == null || mACaddress ==  null) {
      cameraController.getLogger().log("Can't create simplecamera"
          + " object with specified info of camera " + index,
          LogEvent.Type.CRITICAL);
      throw new InvalidCameraTypeException("The right information for "
          + index + " a simple camera is not specified.");
    }
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink(address);
    camera.setMacAddress(mACaddress);
    return camera;
  }

}
