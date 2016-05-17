package com.benine.backend.camera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;

import static org.mockito.Mockito.*;

import java.io.File;

public class CameraCreatorTest {
  
  ServerController serverController;
  CameraController camController = mock(CameraController.class);
  
  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    serverController.setCameraController(camController);
  }
  
  @Test
  public void testCreateIntance() {
    CameraCreator creator = CameraCreator.getInstance();
    assertEquals(creator, CameraCreator.getInstance());
  }
  
  @Test
  public void testCreateCamera() throws InvalidCameraTypeException {
    CameraCreator.getInstance().loadCameras();
    SimpleCamera expected = new SimpleCamera();
    expected.setStreamLink("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=2&scale=100&buffer=100");
    verify(camController).addCamera(expected);
  }
  
  @Test
  public void testCreateCameraNonExcistingType() throws InvalidCameraTypeException {
    CameraCreator.getInstance().loadCameras();
    verify(camController, times(1)).addCamera(any());
  }

}
