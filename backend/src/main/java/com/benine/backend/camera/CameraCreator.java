package com.benine.backend.camera;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;
import com.benine.backend.camera.ipcameracontrol.IPCameraFactory;

import java.util.HashMap;
import java.util.Map;

public class CameraCreator {

  static CameraCreator cameraCreator;
  
  /**
   * Map of string types to right factory.
   */
  static final Map<String, CameraFactory> CAMERA_TYPES;
  
  static {
    CAMERA_TYPES = new HashMap<String, CameraFactory>();
    CAMERA_TYPES.put("simplecamera", new SimpleCameraFactory());
    CAMERA_TYPES.put("ipcamera", new IPCameraFactory());
  }

  /**
   * Private constructor so there can only be one instance of the camera creator.
   */
  private CameraCreator() {

  }
  
  /**
   * Returns the unique instance of the camera creator.
   * @return CameraCreator
   */
  public static synchronized CameraCreator getInstance() {
    if (cameraCreator == null) {
      cameraCreator = new CameraCreator();
    }
    return cameraCreator;
  }

  /**
  * Loads the camera's specified in the config in the camera controller.
  */
  public void loadCameras() {
    Config config = ServerController.getInstance().getConfig();
    int i = 1;
    String type =  config.getValue("camera_" + i + "_type");   
    while (type != null) {
      try {
        ServerController.getInstance().getCameraController().addCamera(createCamera(i, type));
      } catch (InvalidCameraTypeException e) {
        CameraController.logger.log("Camera " + i + " in the config file can not be created",
            LogEvent.Type.CRITICAL);
      }
      i++;
      type = config.getValue("camera_" + i + "_type");
    }  
  }
  
  /**
   * Creates a camera object with the right camera factory.
   * @param index in the config file of the camera.
   * @param type of this camera to create.
   * @return Camera object
   * @throws InvalidCameraTypeException When camera object can't be created.
   */
  private Camera createCamera(int index, String type) throws InvalidCameraTypeException {
    System.out.println(type);
    CameraFactory factory = CAMERA_TYPES.get(type);
    if (factory == null) {
      CameraController.logger.log("The type of camera" + index + "is not specified: " + type,
          LogEvent.Type.CRITICAL);
      throw new InvalidCameraTypeException("Camera type is not regonized");
    }
    CameraController.logger.log("New Camera object is created.",
        LogEvent.Type.INFO);
    return factory.createCamera(index);
  }
  
  
}

