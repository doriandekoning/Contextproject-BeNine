package com.benine.backend.camera;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test class to test the basic cameras.
 */
public abstract class BasicCameraTest {
  
  BasicCamera camera;
  
  @Before
  public void initialize() {
    camera = getCamera();
  }
  
  @Test
  public void testGetSetId() {
    camera.setId(5665);
    Assert.assertEquals(5665, camera.getId());
  }
  
  @Test
  public void testDefaultId() {
    Assert.assertEquals(-1, camera.getId());
  }
  
  @Test
  public void testIsSetInUse() {
    Assert.assertFalse(camera.isInUse());
    camera.setInUse();
    Assert.assertTrue(camera.isInUse());
  }
  
  @Test
  public void testSetNotInUse() {
    Assert.assertFalse(camera.isInUse());
    camera.setInUse();
    Assert.assertTrue(camera.isInUse());
    camera.setNotInUse();
    Assert.assertFalse(camera.isInUse());
  }
  
  @Test
  public void testEqualsCamera() {
    BasicCamera camera2 = getCamera();
    Assert.assertEquals(camera, camera2);
  }
  
  @Test
  public void testEqualsCameraNumber() {
    Assert.assertNotEquals(camera, 1);
  }
  
  @Test
  public void testNotEqualsNull() {
    Assert.assertNotEquals(null, camera);
  }

  
  public abstract BasicCamera getCamera();

}
