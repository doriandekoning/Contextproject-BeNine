package com.benine.backend;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

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
    assertNotEquals(null, serverController.getDatabaseController());
  }
  
  @Test
  public void testGetPresetController() throws Exception {
    assertNotEquals(null, serverController.getPresetController());
  }
  
  @Test
  public void testStartServer() throws Exception {
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
    assertNotEquals(null, serverController.getCameraController());
  }
  
  @Test
  public void testGetLog() throws IOException {  
    Logger expected = mock(Logger.class);
    ServerController spyServerController = Mockito.spy(serverController);
    Mockito.doReturn(expected).when(spyServerController).getLogger();
    assertEquals(expected, spyServerController.getLogger());
  }
  
  @Test
  public void testGetConfig() throws Exception {
	Config expected = ConfigReader.readConfig("resources" + File.separator + "configs" + File.separator + "maintest.conf");
	assertEquals(expected, serverController.getConfig());
  }

}
