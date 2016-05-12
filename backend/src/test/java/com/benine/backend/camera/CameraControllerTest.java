package com.benine.backend.camera;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.StringHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
  @Test
  public void testGetCamerasJSON() throws Exception {
    CameraController controller = new CameraController();
    Camera cam1 = mock(SimpleCamera.class);
    when(cam1.toJSON()).thenReturn("cam1JSON");
    controller.addCamera(cam1);
    Camera cam2 = mock(SimpleCamera.class);
    when(cam2.toJSON()).thenReturn("cam2JSON");
    controller.addCamera(cam2);

    String actualJSON = controller.getCamerasJSON();
    JSONArray ar = new JSONArray();
    ar.add("cam1JSON");
    ar.add("cam2JSON");
    JSONObject obj = new JSONObject();
    obj.put("cameras", ar);
    String expectedJSON = obj.toString();
    Assert.assertEquals(actualJSON, expectedJSON);
  }


}
