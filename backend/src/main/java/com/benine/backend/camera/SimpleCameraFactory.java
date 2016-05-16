package com.benine.backend.camera;

public class SimpleCameraFactory extends CameraFactory {

	@Override
	public Camera createCamera(String[] camSpec) throws InvalidCameraTypeException {
		SimpleCamera camera = new SimpleCamera();
		camera.setStreamLink(camSpec[0]);
		return camera;
	}

}
