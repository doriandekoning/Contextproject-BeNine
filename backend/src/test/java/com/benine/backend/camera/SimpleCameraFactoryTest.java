package com.benine.backend.camera;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;

public class SimpleCameraFactoryTest {
  
  @Test(expected=InvalidCameraTypeException.class)
  public void createCameraNoInfo() throws InvalidCameraTypeException {
    SimpleCameraFactory factory = new SimpleCameraFactory();
    String[] camSpec = new String[0];
    factory.createCamera(camSpec);
  }
  
  @Test(expected=InvalidCameraTypeException.class)
  public void createCameraEmptyArray() throws InvalidCameraTypeException {
    SimpleCameraFactory factory = new SimpleCameraFactory();
    String[] camSpec = null;
    factory.createCamera(camSpec);
  }
  
  @Test(expected=InvalidCameraTypeException.class)
  public void createCameraEmptyFirsElement() throws InvalidCameraTypeException {
    SimpleCameraFactory factory = new SimpleCameraFactory();
    String[] camSpec = new String [1];
    factory.createCamera(camSpec);
  }
  
  @Test
  public void createCamera() throws InvalidCameraTypeException {
    SimpleCameraFactory factory = new SimpleCameraFactory();
    String[] camSpec = {"test.test/camera"};
    Camera result = factory.createCamera(camSpec);
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink("test.test/camera");
    assertEquals(camera, result);
  }

}
