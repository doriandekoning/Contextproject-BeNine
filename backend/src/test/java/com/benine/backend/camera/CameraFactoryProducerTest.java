package com.benine.backend.camera;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;
import com.benine.backend.camera.ipcameracontrol.IPCameraFactory;

public class CameraFactoryProducerTest {
  
  CameraFactoryProducer camFactoryProducer;
  
  @Before
  public void setUp() {
    camFactoryProducer = new CameraFactoryProducer();
  }
  
  @Test
  public void testCreateIPCameraFactory() throws InvalidCameraTypeException {
    assertTrue(camFactoryProducer.getFactory("ipcamera") instanceof IPCameraFactory);
  }
  
  @Test
  public void testCreateSimpleCameraFactory() throws InvalidCameraTypeException {
    assertTrue(camFactoryProducer.getFactory("simplecamera") instanceof SimpleCameraFactory);
  }
  
  @Test(expected = InvalidCameraTypeException.class)
  public void testCreateNonExcistingFactory() throws InvalidCameraTypeException {
    camFactoryProducer.getFactory("non");
  }

}
