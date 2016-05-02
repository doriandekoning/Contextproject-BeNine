package com.benine.ipcameracontrol;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.benine.Camera;
import com.benine.CameraHandler;
import com.benine.CameraHandler.InvalidCameraTypeException;

/**
 * Test class to test the Camera handler wich creates camera objects.
 * @author Bryan
 */
public class CameraHandlerTest {
  
  CameraHandler handler;
  
  @Before
  public void setup(){
    handler = new CameraHandler();
  }
  
  @Test
  public void testIpcameraCreation() throws InvalidCameraTypeException{
    String[] spec = {"ipcamera", "127.0.0.1:3000"};
    Camera camera = handler.createCamera(spec);
    assertTrue(camera instanceof Ipcamera);
  }
  
  @Test(expected = InvalidCameraTypeException.class)
  public void testCameraCreationException() throws InvalidCameraTypeException{
    String[] spec = {"ip2camera", "127.0.0.1:3000"};
    handler.createCamera(spec);
  }

}
