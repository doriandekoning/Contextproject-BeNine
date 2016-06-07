package com.benine.backend.camera;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Config;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CameraFactoryProducerTest {
  
  CameraFactoryProducer camFactoryProducer;
  private Config config = mock(Config.class);
  private CameraController cameraController = mock(CameraController.class);
  private Logger logger = mock(Logger.class);
  
  @Before
  public void setUp() {
    when(config.getValue("camera_1_type")).thenReturn("simplecamera");
    when(config.getValue("camera_1_address")).thenReturn("test");
    when(config.getValue("camera_1_macaddress")).thenReturn("MacAddres");
    when(config.getValue("camera_2_type")).thenReturn("ipcamera");
    when(config.getValue("camera_2_address")).thenReturn("test");
    when(config.getValue("camera_3_type")).thenReturn("ipcamera");
    when(cameraController.getConfig()).thenReturn(config);
    when(cameraController.getLogger()).thenReturn(logger);
    camFactoryProducer = new CameraFactoryProducer(cameraController);
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
  }
  
  @Test
  public void testCreateIPCameraFactory() throws InvalidCameraTypeException {
    Camera camera = camFactoryProducer.getFactory("ipcamera").createCamera(2);
    assertTrue(camera instanceof IPCamera);
  }
  
  @Test
  public void testCreateSimpleCameraFactory() throws InvalidCameraTypeException {
    Camera camera = camFactoryProducer.getFactory("simplecamera").createCamera(1);
    assertTrue(camera instanceof SimpleCamera);
  }
  
  @Test(expected = InvalidCameraTypeException.class)
  public void testCreateSimpleCameraFactoryNotSpecifiedValue() throws InvalidCameraTypeException {
    camFactoryProducer.getFactory("simplecamera").createCamera(3);
  }
  
  @Test(expected = InvalidCameraTypeException.class)
  public void testCreateNonExcistingFactory() throws InvalidCameraTypeException {
    camFactoryProducer.getFactory("non");
  }

}
