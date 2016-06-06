package com.benine.backend.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import com.benine.backend.preset.Preset;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;

public class DatabaseControllerTest {
  
  DatabaseController databaseController;
  Logger logger = mock(Logger.class);
  Database database = mock(Database.class);
  
  @Before
  public void setup() {    
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    ServerController.getInstance();
    when(database.getAllPresets()).thenReturn(new ArrayList<>());
    databaseController = new DatabaseController();
    databaseController.setDatabase(database);
  }
  
  @Test
  public void testStartServer() {
    when(database.checkDatabase()).thenReturn(false);
    databaseController.start();
    verify(database).resetDatabase();
  }
  
  @Test
  public void testStartServerExcisting() {
    when(database.checkDatabase()).thenReturn(true);
    databaseController.start();
    verify(database).useDatabase();
  }
  
  @Test
  public void testStopServer() {
    databaseController.stop();
    verify(database).closeConnection();
  }
  
  @Test
  public void testLoadPresets() {
    when(database.checkDatabase()).thenReturn(false);
    ArrayList<Preset> presets = new ArrayList<>();
    ArrayList<String> tags = new ArrayList<>();
    tags.add("test");
    Preset preset = mock(Preset.class);
    presets.add(preset);
    when(database.getTagsFromPreset(preset)).thenReturn(tags);
  }
  
  @Test
  public void testLoadPresetsException() throws SQLException {
    when(database.checkDatabase()).thenReturn(false);
    Exception expected = new SQLException();
    databaseController.start();
  }
  
  @Test
  public void testGetDatabase() {
    Assert.assertEquals(database, databaseController.getDatabase());
  }


}
