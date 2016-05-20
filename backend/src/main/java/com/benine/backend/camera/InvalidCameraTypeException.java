package com.benine.backend.camera;

/**
* Exception thrown when camera object type is not available.
*/
public class InvalidCameraTypeException extends Exception {

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