package com.benine.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.database.Database;

import static org.mockito.Mockito.mock;


/**
 * Test for the Server Controller class.
 *
 */
public class ServerControllerTest {
  
  ServerController serverController;
  
  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "serverControllertest.conf");
    serverController = ServerController.getInstance();
  }
  
  @Test
  public void testGetDatabase() throws Exception {
    Database database = mock(Database.class);
    serverController.setDatabase(database);
    assertEquals(database, serverController.getDatabase());
  }
  
  @Test
  public void testStartServer() {
    serverController.setDatabase(mock(Database.class));
    serverController.start();  
    assertTrue(serverController.isServerRunning());
    serverController.stop();
  }
  
  @Test
  public void testStopServer() {
    serverController.stop();
    assertFalse(serverController.isServerRunning());
  }
  
  @Test
  public void testGetLog() {  
    Logger logger = mock(Logger.class);
    serverController.setLogger(logger);
    assertEquals(logger, serverController.getLogger());
  }
  
  @Test
  public void testGetConfig() throws Exception {
    assertEquals(ConfigReader.readConfig("resources" + File.separator + "configs" + File.separator + "serverControllertest.conf"), serverController.getConfig());
  }

}
