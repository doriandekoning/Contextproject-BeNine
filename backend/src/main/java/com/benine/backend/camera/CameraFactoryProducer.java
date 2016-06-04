package com.benine.backend.camera;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.ipcameracontrol.IPCameraFactory;

import java.util.HashMap;
import java.util.Map;

public class CameraFactoryProducer {
  
  private CameraController cameraController;
  
  /**
   * Constructs a camera factory producer.
   * @param cameraController to interact with the rest of the system.
   */
  public CameraFactoryProducer(CameraController cameraController) {
    this.cameraController = cameraController;
    CAMERA_TYPES.put("simplecamera", new SimpleCameraFactory(cameraController));
    CAMERA_TYPES.put("ipcamera", new IPCameraFactory(cameraController));
  }
  
  /**
   * Map of string types to right factory.
   */
  static final Map<String, CameraFactory> CAMERA_TYPES;
  
  static {
    CAMERA_TYPES = new HashMap<>();
  }
  
  /**
   * Returns the right camera factory.
   * @param type of the camera factory that is requested.
   * @return the right camera factory.
   * @throws InvalidCameraTypeException when the type does not exists.
   */
  public CameraFactory getFactory(String type) throws InvalidCameraTypeException {
    if (CAMERA_TYPES.get(type) == null) {
      cameraController.getLogger().log("The following type is not specified: " + type,
          LogEvent.Type.CRITICAL);
      throw new InvalidCameraTypeException("Camera type is not regonized");
    }
    return (index) -> CAMERA_TYPES.get(type).createCamera(index);
  }
}
