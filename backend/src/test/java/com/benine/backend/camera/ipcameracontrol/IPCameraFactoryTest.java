package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.ipcameracontrol.IPCameraFactory;
import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;

/**
 * Test class to test the Camera handler wich creates camera objects.
 * @author Bryan
 */
public class IPCameraFactoryTest {
  
  IPCameraFactory handler;
  
  @Before
  public void setup(){
    handler = new IPCameraFactory();
  }
  
  @Test
  public void testIpcameraCreation() throws InvalidCameraTypeException{
    String[] spec = {"ipcamera", "127.0.0.1:3000"};
    Camera camera = handler.createCamera(spec);
    assertTrue(camera instanceof Camera);
  }
  
  @Test(expected = InvalidCameraTypeException.class)
  public void testCameraCreationException() throws InvalidCameraTypeException{
    String[] spec = {"ip2camera", "127.0.0.1:3000"};
    handler.createCamera(spec);
  }

}
