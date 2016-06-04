package com.benine.backend.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.sql.SQLException;
import java.util.ArrayList;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;
import com.benine.backend.Logger;

public class DatabaseControllerTest {
  
  DatabaseController databaseController;
  ServerController serverController;
  PresetController presetController;
  Logger logger;
  Database database;
  
  @Before
  public void setup() {
    database = mock(Database.class);
    when(database.getAllPresets()).thenReturn(new ArrayList<>());
    serverController = mock(ServerController.class);
    when(serverController.getConfig()).thenReturn(mock(Config.class));
    logger = mock(Logger.class);
    when(serverController.getLogger()).thenReturn(logger);
    presetController = mock(PresetController.class);
    when(serverController.getPresetController()).thenReturn(presetController);
    databaseController = new DatabaseController(serverController);
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
    when(presetController.getPresets()).thenReturn(presets);
    databaseController.start();
    verify(preset).addTags(tags);
  }
  
  @Test
  public void testLoadPresetsException() throws SQLException {
    when(database.checkDatabase()).thenReturn(false);
    doThrow(new SQLException()).when(presetController).addPresets(new ArrayList<Preset>());
    databaseController.start();
    verify(logger).log("Cannot read presets from database", LogEvent.Type.CRITICAL);
  }
  
  @Test
  public void testGetDatabase() {
    Assert.assertEquals(database, databaseController.getDatabase());
  }


}
