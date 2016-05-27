package com.benine.backend.camera;

import com.benine.backend.ServerController;
import com.benine.backend.database.Database;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on 5-5-16.
 */
public class CameraControllerTest {

  private final Database database = mock(Database.class);
  private CameraController controller;
  private ServerController serverController;

  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    
    serverController.setDatabase(database);
    controller = new CameraController();
  }
  
  @Test
  public void testLoadConfigCameras() {
    controller.loadConfigCameras();
    assertTrue(controller.getCameras().size() == 1);
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
