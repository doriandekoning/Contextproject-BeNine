package com.benine.backend.camera;


public abstract class CameraFactory {
	
	/**
	* Creates a camera object as specified in camSpec.
	* @param camSpec specification of the camera 0: type, 1: additional info.
	* @return Camera object.
	* @throws InvalidCameraTypeException when specified camera type can not be created.
	*/
	public abstract Camera createCamera(String[] camSpec) throws InvalidCameraTypeException;
	
	/**
	* Exception thrown when camera object type is not available.
	*/
	public static class InvalidCameraTypeException extends Exception {
	    
		/**
	    * Serial version.
	    */
		private static final long serialVersionUID = 10863715123938677L;

	    public InvalidCameraTypeException(String reason) {
	      super(reason);
	    }
	  }

}
