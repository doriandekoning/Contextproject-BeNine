package com.benine.backend.database;

import com.benine.backend.performance.PresetQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    Set<String> tags = new HashSet<>();
    tags.add("test");
    Preset preset = mock(Preset.class);
    when(preset.getName()).thenReturn("");
    presets.add(preset);
    when(database.getAllPresets()).thenReturn(presets);
    databaseController.start();
    Assert.assertTrue(ServerController.getInstance().getPresetController().getPresets().contains(preset));
  }

  @Test
  public void testLoadPresetQueues() {
    when(database.checkDatabase()).thenReturn(false);
    ArrayList<PresetQueue> queueList = new ArrayList<>();
    PresetQueue queue = mock(PresetQueue.class);
    queueList.add(queue);
    when(database.getQueues()).thenReturn(queueList);
    databaseController.start();
    Assert.assertTrue(ServerController.getInstance().getPresetQueueController().getPresetQueues().contains(queue));
  }
  
  @Test
  public void testGetDatabase() {
    Assert.assertEquals(database, databaseController.getDatabase());
  }


}
