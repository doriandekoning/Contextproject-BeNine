package com.benine.backend.camera;

public interface CameraFactory {

  /**
  * Creates a camera object as specified in camSpec.
  * @param index of the camera in the config file to create.
  * @return Camera object.
  * @throws InvalidCameraTypeException when specified camera type can not be created.
  */
  Camera createCamera(int index) throws InvalidCameraTypeException;
  
}
