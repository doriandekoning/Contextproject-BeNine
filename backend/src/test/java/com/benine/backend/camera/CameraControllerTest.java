package com.benine.backend.camera;

import com.benine.backend.Main;
import com.benine.backend.Preset;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.Database;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

/**
 * Created by dorian on 5-5-16.
 */
public class CameraControllerTest {

  private final Database database = mock(Database.class);

  @Before
  public void setUp() {
    Main.setDatabase(database);
  }

  @Test
  public void testGetCameraById() {
    CameraController controller = new CameraController();
    Camera cam = new SimpleCamera();
    controller.addCamera(cam);
    Assert.assertEquals(controller.getCameraById(0), cam);

  }

  @Test
  public void testAddPreset() throws SQLException {
    CameraController controller = new CameraController();
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(0,0,0,0,0,false,0,0,false);
    controller.addPreset(0, preset);
    Mockito.verify(database).addPreset(0, 0, preset);
    Mockito.verifyZeroInteractions(database);
  }

  @Test
  public void testAddPresetAtPosition() throws SQLException {
    CameraController controller = new CameraController();
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(0,0,0,0,0,false,0,0,false);
    controller.addPresetAtPosition(0, preset, 0);
    Mockito.verify(database).addPreset(0, 0, preset);
    Mockito.verifyZeroInteractions(database);
  }

  @Test
  public void testGetPresetsFromDatabase() throws SQLException {
    CameraController controller = new CameraController();
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(0,0,0,0,0,false,0,0,false);
    controller.getPresetsFromDatabase();
    Mockito.verify(database).getAllPresetsCamera(0);
    Mockito.verifyZeroInteractions(database);
  }

  @Test
  public void testResetPresetsInDatabase() throws SQLException {
    CameraController controller = new CameraController();
    controller.addCamera(new IPCamera("ip"));
    Preset preset = new Preset(0,0,0,0,0,false,0,0,false);
    controller.addPreset(0, preset);
    controller.resetPresetsInDatabase();
    Mockito.verify(database).resetDatabase();
    Mockito.verify(database).addCamera(0, "ip");
    Mockito.verify(database, times(2)).addPreset(0, 0, preset);
    Mockito.verifyZeroInteractions(database);
  }

  @Test
  public void testGetPreset() throws SQLException {
    CameraController controller = new CameraController();
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(0,0,0,0,0,false,0,0,false);
    controller.addPreset(0, preset);
    Assert.assertEquals(preset, controller.getPreset(0, 0));
  }

  @Test
  public void testResetPresets() throws SQLException {
    CameraController controller = new CameraController();
    controller.addCamera(new SimpleCamera());
    Preset preset = new Preset(0,0,0,0,0,false,0,0,false);
    controller.addPreset(0, preset);
    controller.resetPresets();
    Assert.assertNull(controller.getPreset(0,0));
  }

}
