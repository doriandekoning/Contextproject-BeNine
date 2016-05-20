package com.benine.backend.camera;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.ServerController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

public class CameraFactoryProducerTest {
  
  CameraFactoryProducer camFactoryProducer;
  
  @Before
  public void setUp() {
    camFactoryProducer = new CameraFactoryProducer();
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
  }
  
  @Test
  public void testCreateIPCameraFactory() throws InvalidCameraTypeException {
    Camera camera = camFactoryProducer.getFactory("ipcamera").createCamera(2);
    assertTrue(camera instanceof IPCamera);
  }
  
  @Test
  public void testCreateSimpleCameraFactory() throws InvalidCameraTypeException {
    Camera camera = camFactoryProducer.getFactory("simplecamera").createCamera(2);
    assertTrue(camera instanceof SimpleCamera);
  }
  
  @Test(expected = InvalidCameraTypeException.class)
  public void testCreateNonExcistingFactory() throws InvalidCameraTypeException {
    camFactoryProducer.getFactory("non");
  }

}
