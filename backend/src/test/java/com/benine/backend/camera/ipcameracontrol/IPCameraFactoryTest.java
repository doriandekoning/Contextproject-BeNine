package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Config;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.InvalidCameraTypeException;
import com.benine.backend.camera.ipcameracontrol.IPCameraFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class to test the Camera handler which creates camera objects.
 */
public class IPCameraFactoryTest {
  
  private IPCameraFactory handler;
  private CameraController cameraController = mock(CameraController.class);
  private Config config = mock(Config.class);
  private Logger logger = mock(Logger.class);
  
  @Before
  public void setUp() {
    when(config.getValue("camera_1_type")).thenReturn("simplecamera");
    when(config.getValue("camera_1_address")).thenReturn("test");
    when(config.getValue("camera_1_macaddress")).thenReturn("MacAddres");
    when(config.getValue("camera_2_type")).thenReturn("ipcamera");
    when(config.getValue("camera_2_address")).thenReturn("test");
    when(config.getValue("camera_2_aspectratio")).thenReturn("32.3");
    when(config.getValue("camera_3_type")).thenReturn("ipcamera");
    when(cameraController.getConfig()).thenReturn(config);
    when(cameraController.getLogger()).thenReturn(logger);
    handler = new IPCameraFactory(cameraController);
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

  @Test
  public void testDefaultAspectRatio() throws InvalidCameraTypeException {
    IPCamera camera = handler.createCamera(1);
    assertEquals(16.0/9, camera.getAspectRatio(), 0.05);
  }

  @Test
  public void testAspectRatio() throws InvalidCameraTypeException {
    IPCamera camera = handler.createCamera(2);
    assertEquals(32.3, camera.getAspectRatio(), 0.05);
  }
}
