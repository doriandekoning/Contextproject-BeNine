package com.benine.backend.camera;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the camera busy Exception.
 */
public class CameraBusyExceptionTest {

  CameraBusyException exception;

  @Before
  public void setUp() {
    exception = new CameraBusyException("Message", 1);
  }

  @Test
  public void testGetCameraId() {
    Assert.assertEquals(1, exception.getCamId());
  }
}
