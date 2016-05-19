package com.benine.backend.camera;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;

/**
 * Factory for the creation of a simple camera object.
 *
 */
public class SimpleCameraFactory extends CameraFactory {

  @Override
  public Camera createCamera(int index) throws InvalidCameraTypeException {
    Config config = ServerController.getInstance().getConfig();
    String address = config.getValue("camera_" + index + "_address");
    if (address == null) {
      CameraController.logger.log("Can't create simplecamera"
          + " object with specified info of camera " + index,
          LogEvent.Type.CRITICAL);
      throw new InvalidCameraTypeException("The right information for"
          + " a simple camera is not specified." + config.getValue("camera_1_address"));
    }
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink(address);
    return camera;
  }

}
