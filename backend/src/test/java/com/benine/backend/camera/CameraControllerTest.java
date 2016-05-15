package com.benine.backend.camera;

import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.Database;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Created by dorian on 5-5-16.
 */
public class CameraControllerTest {

  private final Database database = mock(Database.class);
  private CameraController controller;
  private ServerController serverController = mock(ServerController.class);

  @Before
  public void setUp() {
    when(serverController.getDatabase()).thenReturn(database);
    controller = new CameraController();
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
  public void testAddPreset() throws SQLException {
    Camera cam1 = new SimpleCamera();

    controller.addCamera(cam1);
    Preset preset = new Preset(new Position(0,0), 0,0,0,false,0,0,false);
    controller.addPreset(1, preset);

    Mockito.verify(database).addPreset(1, 0, preset);
    Mockito.verifyZeroInteractions(database);
  }

  @Test
  public void testAddPresetNoSpaceAvailable() throws SQLException {
    Camera cam1 = new SimpleCamera();

    controller.addCamera(cam1);
    Preset preset = new Preset(new Position(0,0), 0,0,0,false,0,0,false);

    Preset[] empty = new Preset[0];
    controller.getCameraById(1).setPresets(empty);

    Assert.assertEquals(-1, controller.addPreset(1, preset));
  }

  @Test
  public void testAddPresetAtPosition() throws SQLException {
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(new Position(0,0), 0,0,0,false,0,0,false);
    controller.addPresetAtPosition(1, preset, 0);
    Mockito.verify(database).addPreset(1, 0, preset);
    Mockito.verifyZeroInteractions(database);
  }

  @Test
  public void testAddPresetAtPositionUpdate() throws SQLException {
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(new Position(0,0), 0,0,0,false,0,0,false);
    Preset newPreset = new Preset(new Position(1,1), 1,1,1,false,0,0,false);

    Preset[] presetList = new Preset[1];
    presetList[0] = preset;
    controller.getCameraById(1).setPresets(presetList);

    controller.addPresetAtPosition(1, newPreset, 0);

    Mockito.verify(database).updatePreset(1, 0, newPreset);
  }

  @Test
  public void testGetPresetsFromDatabase() throws SQLException {
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(new Position(0,0), 0,0,0,false,0,0,false);
    controller.getPresetsFromDatabase();
    Mockito.verify(database).getAllPresetsCamera(1);
    Mockito.verifyZeroInteractions(database);
  }

  @Test
  public void testResetPresetsInDatabase() throws SQLException {
    controller.addCamera(new IPCamera("ip"));
    Preset preset = new Preset(new Position(0,0), 0,0,0,false,0,0,false);
    controller.addPreset(1, preset);
    controller.resetPresetsInDatabase();
    Mockito.verify(database).resetDatabase();
    Mockito.verify(database).addCamera(1, "ip");
    Mockito.verify(database, times(2)).addPreset(1, 0, preset);
    Mockito.verifyZeroInteractions(database);
  }

  @Test
  public void testResetPresetsInDatabaseNotIp() throws SQLException {
    SimpleCamera cameramock = mock(SimpleCamera.class);
    controller.addCamera(cameramock);
    Mockito.reset(cameramock);

    controller.resetPresetsInDatabase();

    Mockito.verifyZeroInteractions(cameramock);
  }

  @Test
  public void testGetPreset() throws SQLException {
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(new Position(0,0), 0,0,0,false,0,0,false);
    controller.addPreset(1, preset);
    Assert.assertEquals(preset, controller.getPreset(1, 0));
  }

  @Test
  public void testResetPresets() throws SQLException {
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(new Position(0,0), 0,0,0,false,0,0,false);
    controller.addPreset(1, preset);
    controller.resetPresets();
    Assert.assertNull(controller.getPreset(1,0));
  }

}
