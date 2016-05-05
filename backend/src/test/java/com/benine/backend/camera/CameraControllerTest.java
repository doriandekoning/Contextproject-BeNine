package com.benine.backend.camera;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dorian on 5-5-16.
 */
public class CameraControllerTest {

  @Test
  public void testGetCameraById() {
    CameraController controller = new CameraController();
    Camera cam = new SimpleCamera();
    controller.addCamera(cam);
    Assert.assertEquals(controller.getCameraById(0), cam);

  }

}
