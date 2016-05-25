package com.benine.backend;

import com.benine.backend.camera.CameraController;
import com.benine.backend.database.Database;
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
  public void testGetDatabase() throws Exception {
    Database database = mock(Database.class);
    serverController.setDatabase(database);
    assertEquals(database, serverController.getDatabase());
  }
  
  @Test
  public void testStartServer() throws Exception {
    serverController.setDatabase(mock(Database.class));
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
    assertEquals(ConfigReader.readConfig("resources" + File.separator + "configs" + File.separator + "maintest.conf"), serverController.getConfig());
  }

}
