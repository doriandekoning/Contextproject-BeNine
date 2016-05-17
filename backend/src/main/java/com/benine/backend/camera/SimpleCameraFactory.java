package com.benine.backend.camera;

import com.benine.backend.LogEvent;

/**
 * Factory for the creation of a simple camera object.
 *
 */
public class SimpleCameraFactory extends CameraFactory {

  @Override
  public Camera createCamera(String[] camSpec) throws InvalidCameraTypeException {
    if (camSpec == null || camSpec.length < 1 || camSpec[0] == null) {
      CameraController.logger.log("Can't create simplecamera object with specified info.",
          LogEvent.Type.CRITICAL);
      throw new InvalidCameraTypeException(
          "The right information for a simple camera is not specified.");
    }
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink(camSpec[0]);
    return camera;
  }

}
