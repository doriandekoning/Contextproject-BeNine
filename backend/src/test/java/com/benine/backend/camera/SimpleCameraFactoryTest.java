package com.benine.backend.camera;

import com.benine.backend.Config;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleCameraFactoryTest {
  
  ServerController serverController;
  private CameraController cameraController = mock(CameraController.class);
  private Config config = mock(Config.class);
  private Logger logger = mock(Logger.class);
  SimpleCameraFactory factory;
  
  @Before
  public void setUp() {
    when(config.getValue("camera_2_type")).thenReturn("simpleCamera");
    when(config.getValue("camera_2_address")).thenReturn("test");
    when(config.getValue("camera_2_macaddress")).thenReturn("test");
    when(config.getValue("camera_3_type")).thenReturn("ipcamera");
    when(cameraController.getConfig()).thenReturn(config);
    when(cameraController.getLogger()).thenReturn(logger);
    
    factory = new SimpleCameraFactory(cameraController);
  }

  
  @Test(expected=InvalidCameraTypeException.class)
  public void createCameraNoInfo() throws InvalidCameraTypeException {
    factory.createCamera(3);
  }
  
  @Test
  public void createCamera() throws InvalidCameraTypeException {
    Camera result = factory.createCamera(2);
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink("test");
    camera.setMacAddress("test");
    assertEquals(camera, result);
  }

}
