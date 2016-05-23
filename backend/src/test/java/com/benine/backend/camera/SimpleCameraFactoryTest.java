package com.benine.backend.camera;

import com.benine.backend.ServerController;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SimpleCameraFactoryTest {
  
  ServerController serverController;
  
  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    
    CameraController camController = new CameraController();
    serverController.setCameraController(camController);
  }

  
  @Test(expected=InvalidCameraTypeException.class)
  public void createCameraNoInfo() throws InvalidCameraTypeException {
    SimpleCameraFactory factory = new SimpleCameraFactory();
    factory.createCamera(3);
  }
  
  @Test
  public void createCamera() throws InvalidCameraTypeException {
    SimpleCameraFactory factory = new SimpleCameraFactory();
    Camera result = factory.createCamera(2);
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink("http://test");
    camera.setMacAddress("simplecamera3");
    assertEquals(camera, result);
  }

}
