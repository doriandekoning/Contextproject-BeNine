package com.benine.backend.camera;


public abstract class CameraFactory {

  /**
  * Creates a camera object as specified in camSpec.
  * @param index of the camera in the config file to create.
  * @return Camera object.
  * @throws InvalidCameraTypeException when specified camera type can not be created.
  */
  public abstract Camera createCamera(int index) throws InvalidCameraTypeException;

  /**
  * Exception thrown when camera object type is not available.
  */
  public static class InvalidCameraTypeException extends Exception {

    /**
      * Serial version.
      */
    private static final long serialVersionUID = 10863715123938677L;

    /**
     * InvalidCameraTypeException used when the type of a camera does not exist.
     * @param reason why the exception is thrown.
     */
    public InvalidCameraTypeException(String reason) {
        super(reason);
    }
  }
}
