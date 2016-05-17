package com.benine.backend.camera;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;

public class CameraCreatorTest {
  
  @Test
  public void testCreateIntance() {
    CameraCreator creator = CameraCreator.getInstance();
    assertEquals(creator, CameraCreator.getInstance());
  }
  
  @Test
  public void testCreateCamera() throws InvalidCameraTypeException {
    String[] spec = {"simplecamera", "testlink"};
    Camera camera = CameraCreator.getInstance().createCamera(spec);
    SimpleCamera expected = new SimpleCamera();
    expected.setStreamLink("testlink");
    assertEquals(expected, camera);
  }
  
  @Test(expected=InvalidCameraTypeException.class)
  public void testCreateCameraNonExcistingType() throws InvalidCameraTypeException {
    String[] spec = {"simple", "testlink"};
    CameraCreator.getInstance().createCamera(spec);
  }

}
