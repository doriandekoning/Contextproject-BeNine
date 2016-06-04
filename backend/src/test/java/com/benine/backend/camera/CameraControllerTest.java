package com.benine.backend.camera;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.video.StreamController;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Created on 5-5-16.
 */
public class CameraControllerTest {

  private CameraController controller;
  private ServerController serverController = mock(ServerController.class);
  private Config config = mock(Config.class);
  private Logger logger = mock(Logger.class);
  private StreamController streamController = mock(StreamController.class);

  @Before
  public void setUp() {
    when(config.getValue("camera_1_type")).thenReturn("simplecamera");
    when(config.getValue("camera_1_address")).thenReturn("test");
    when(config.getValue("camera_1_macaddress")).thenReturn("MacAddres");
    when(serverController.getConfig()).thenReturn(config);
    when(serverController.getLogger()).thenReturn(logger);
    when(serverController.getStreamController()).thenReturn(streamController);
    controller = new CameraController(serverController);
  }
  
  @Test
  public void testLoadConfigCameras() {
    controller.loadConfigCameras();
    assertTrue(controller.getCameras().size() == 1);
  }
  
  @Test
  public void testLoadConfigCamerasExcpetion() {
    when(config.getValue("camera_2_type")).thenReturn("simplecamera");
    controller.loadConfigCameras();
    verify(logger).log("Camera: 2 from the config can not be created.",
        LogEvent.Type.WARNING);
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
