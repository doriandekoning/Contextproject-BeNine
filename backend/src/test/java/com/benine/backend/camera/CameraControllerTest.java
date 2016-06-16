package com.benine.backend.camera;

import com.benine.backend.ServerController;
import com.benine.backend.database.MySQLDatabase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on 5-5-16.
 */
public class CameraControllerTest {

  private CameraController controller;

  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    ServerController.getInstance();
    ServerController.getInstance().getDatabaseController().setDatabase(mock(MySQLDatabase.class));
    controller = new CameraController();
  }
  
  @Test
  public void testLoadConfigCameras() {
    controller.loadConfigCameras();
    assertEquals(1, controller.getCameras().size());
  }

  @Test
  public void testGetCameraById() {
    Camera cam1 = new SimpleCamera();
    Camera cam2 = new SimpleCamera();

    controller.addCamera(cam1);
    controller.addCamera(cam2);

    Assert.assertEquals(cam2, controller.getCameraById(2));
  }

  @Test
  public void testGetCameraByIdNotFound() {
    Camera cam1 = new SimpleCamera();
    Camera cam2 = new SimpleCamera();

    controller.addCamera(cam1);
    controller.addCamera(cam2);

    Assert.assertNull(controller.getCameraById(3));
  }

  @Test
  public void testGetCamerasJSON() throws Exception {
    Camera cam1 = mock(SimpleCamera.class);
    JSONObject ob1 = new JSONObject();
    ob1.put("id", "cam1JSON");
    when(cam1.toJSON()).thenReturn(ob1);
    controller.addCamera(cam1);
    Camera cam2 = mock(SimpleCamera.class);
    JSONObject ob2 = new JSONObject();
    ob2.put("id", "cam2JSON");
    when(cam2.toJSON()).thenReturn(ob2);
    controller.addCamera(cam2);

    String actualJSON = controller.getCamerasJSON();
    JSONArray ar = new JSONArray();
    ar.add(ob1);
    ar.add(ob2);
    JSONObject obj = new JSONObject();
    obj.put("cameras", ar);
    String expectedJSON = obj.toString();
    Assert.assertEquals(actualJSON, expectedJSON);
  }
  
  @Test
  public void testGetCamerasJSONException() throws Exception {
    Camera cam1 = mock(SimpleCamera.class);
    when(cam1.toJSON()).thenThrow(new CameraConnectionException("camera test exception", -1));
    when(cam1.getId()).thenReturn(1);
    controller.addCamera(cam1);
    
    String actualJSON = controller.getCamerasJSON();
    JSONArray ar = new JSONArray();
    JSONObject jsonCamera = new JSONObject();
    jsonCamera.put("unavailable", true);
    jsonCamera.put("id", 1);
    ar.add(jsonCamera);
    JSONObject obj = new JSONObject();
    obj.put("cameras", ar);
    String expectedJSON = obj.toString();
    Assert.assertEquals(expectedJSON, actualJSON);
  }

  @Test
  public void testGetCamerasInUse() {
    Camera cam1 = mock(SimpleCamera.class);
    when(cam1.isInUse()).thenReturn(true);
    Camera cam2 = mock(SimpleCamera.class);
    when(cam2.isInUse()).thenReturn(false);
    controller.addCamera(cam1);
    controller.addCamera(cam2);
    List<Camera> expected = new ArrayList<Camera>();
    expected.add(cam1);
    Assert.assertEquals(expected, controller.camerasInUse());
  }

}
