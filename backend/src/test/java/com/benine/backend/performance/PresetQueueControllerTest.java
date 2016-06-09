package com.benine.backend.performance;

import java.io.File;
import java.util.ArrayList;

import com.benine.backend.database.MySQLDatabase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.ServerController;

import static org.mockito.Mockito.*;

public class PresetQueueControllerTest {
  
  PresetQueueController presetQueueController = new PresetQueueController();
  PresetQueue presetQueue1;
  PresetQueue presetQueue3;
  
  @Before
  public void setup() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    ServerController.getInstance();
    presetQueue1 = mock(PresetQueue.class);
    when(presetQueue1.getID()).thenReturn(-1);
    JSONObject json1 = new JSONObject();
    json1.put("id", 1);
    json1.put("name", "testQueue");
    when(presetQueue1.toJSON()).thenReturn(json1);
    presetQueue3 = mock(PresetQueue.class);
    when(presetQueue3.getID()).thenReturn(3);
    JSONObject json3 = new JSONObject();
    json3.put("id", 3);
    json3.put("name", "testQueue");
    when(presetQueue3.toJSON()).thenReturn(json3);
    ServerController.getInstance().getDatabaseController().setDatabase(mock(MySQLDatabase.class));
  }
  
  @Test
  public void testAddPresetQueue() {  
    presetQueueController.addPresetQueue(presetQueue1);
    ArrayList<PresetQueue> expected = new ArrayList<PresetQueue>();
    expected.add(presetQueue1);
    Assert.assertEquals(expected, presetQueueController.getPresetQueues());
  }
  
  @Test
  public void testAddPresetQueues() {  
    ArrayList<PresetQueue> expected = new ArrayList<PresetQueue>();
    expected.add(presetQueue1);
    expected.add(presetQueue3);
    presetQueueController.addPresetQueues(expected);
    Assert.assertEquals(expected, presetQueueController.getPresetQueues());
  }
  
  @Test
  public void testRemovePresetQueue() {  
    presetQueueController.addPresetQueue(presetQueue1);
    presetQueueController.addPresetQueue(presetQueue3);
    ArrayList<PresetQueue> expected = new ArrayList<PresetQueue>();
    expected.add(presetQueue1);
    presetQueueController.removePresetQueue(presetQueue3);
    Assert.assertEquals(expected, presetQueueController.getPresetQueues());
  }
  
  @Test
  public void testUpdatePresetQueue() {  
    presetQueueController.addPresetQueue(presetQueue3);
    PresetQueue updatedPresetQueue = mock(PresetQueue.class);
    when(updatedPresetQueue .getID()).thenReturn(3);
    presetQueueController.updatePresetQueue(updatedPresetQueue );
    ArrayList<PresetQueue> expected = new ArrayList<PresetQueue>();
    expected.add(updatedPresetQueue);
    Assert.assertEquals(expected, presetQueueController.getPresetQueues());
  }
  
  @Test
  public void testAddPresetQueuewithID() {
    presetQueueController.addPresetQueue(presetQueue3);
    ArrayList<PresetQueue> expected = new ArrayList<PresetQueue>();
    expected.add(presetQueue3);
    Assert.assertEquals(expected, presetQueueController.getPresetQueues());
  }
  
  @Test
  public void testgetPresetQueueById() {
    presetQueueController.addPresetQueue(presetQueue3);
    Assert.assertEquals(presetQueue3, presetQueueController.getPresetQueueById(3));
  }
  
  @Test
  public void testgetPresetQueueByIdNonExcisting() {
    presetQueueController.addPresetQueue(presetQueue3);
    Assert.assertEquals(null, presetQueueController.getPresetQueueById(5));
  }
  
  @Test
  public void testgetPresetQueueJSON() {
    presetQueueController.addPresetQueue(presetQueue1);
    presetQueueController.addPresetQueue(presetQueue3);
    JSONObject expected = new JSONObject();
    JSONArray array = new JSONArray();
    array.add(presetQueue1.toJSON());
    array.add(presetQueue3.toJSON());
    expected.put("presetqueues", array);
    Assert.assertEquals(expected.toJSONString(), presetQueueController.getPresetQueueJSON());
  }

}
