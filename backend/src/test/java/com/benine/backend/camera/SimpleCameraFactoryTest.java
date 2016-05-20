package com.benine.backend.camera;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.ServerController;

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
    camera.setStreamLink("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=2&scale=100&buffer=100");
    camera.setMacAddress("simplecamera3");
    assertEquals(camera, result);
  }

}
