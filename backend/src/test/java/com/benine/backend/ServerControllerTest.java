package com.benine.backend;

import com.benine.backend.camera.CameraController;
import com.benine.backend.database.DatabaseController;
import com.benine.backend.preset.PresetController;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


/**
 * Test for the Server Controller class.
 *
 */
public class ServerControllerTest {
  
  ServerController serverController;
  
  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
  }
  
  @Test
  public void testGetDatabaseController() throws Exception {
    DatabaseController database = mock(DatabaseController.class);
    serverController.setDatabaseController(database);
    assertEquals(database, serverController.getDatabaseController());
  }
  
  @Test
  public void testGetPresetController() throws Exception {
    PresetController presetController = mock(PresetController.class);
    serverController.setPresetController(presetController);
    assertEquals(presetController, serverController.getPresetController());
  }
  
  @Test
  public void testStartServer() throws Exception {
    serverController.setDatabaseController(mock(DatabaseController.class));
    serverController.start();  
    assertTrue(serverController.isServerRunning());
    serverController.stop();
  }
  
  @Test
  public void testStopServer() throws Exception {
    serverController.stop();
    assertFalse(serverController.isServerRunning());
  }
  
  @Test
  public void testSetcameraController() {
    CameraController camController = mock(CameraController.class);
    serverController.setCameraController(camController);
    assertEquals(camController, serverController.getCameraController());
  }
  
  @Test
  public void testGetLog() {  
    Logger logger = mock(Logger.class);
    serverController.setLogger(logger);
    assertEquals(logger, serverController.getLogger());
  }
  
  @Test
  public void testGetConfig() throws Exception {
	Config expected = ConfigReader.readConfig("resources" + File.separator + "configs" + File.separator + "maintest.conf");
	assertEquals(expected, serverController.getConfig());
  }

}
