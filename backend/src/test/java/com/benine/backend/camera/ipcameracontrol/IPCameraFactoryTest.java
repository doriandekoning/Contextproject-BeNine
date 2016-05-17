package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ipcameracontrol.IPCameraFactory;
import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;

/**
 * Test class to test the Camera handler wich creates camera objects.
 * @author Bryan
 */
public class IPCameraFactoryTest {
  
  IPCameraFactory handler;
  
  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    ServerController serverController = ServerController.getInstance();
    
    CameraController camController = new CameraController();
    serverController.setCameraController(camController);
    handler = new IPCameraFactory();
  }
  
  @Test
  public void testIpcameraCreation() throws InvalidCameraTypeException{
    Camera camera = handler.createCamera(1);
    assertTrue(camera instanceof Camera);
  }
  
  @Test(expected = InvalidCameraTypeException.class)
  public void testCameraCreationException() throws InvalidCameraTypeException{
    handler.createCamera(3);
  }
}
