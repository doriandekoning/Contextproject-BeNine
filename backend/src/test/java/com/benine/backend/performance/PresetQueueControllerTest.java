package com.benine.backend.performance;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PresetQueueControllerTest {
  
  PresetQueueController presetQueueController = new PresetQueueController();
  PresetQueue presetQueue1;
  PresetQueue presetQueue3;
  
  @Before
  public void setup() {
    presetQueue1 = mock(PresetQueue.class);
    when(presetQueue1.getId()).thenReturn(-1);
    JSONObject json1 = new JSONObject();
    json1.put("id", 1);
    json1.put("name", "testQueue");
    when(presetQueue1.toJSON()).thenReturn(json1);
    presetQueue3 = mock(PresetQueue.class);
    when(presetQueue3.getId()).thenReturn(3);
    JSONObject json3 = new JSONObject();
    json3.put("id", 3);
    json3.put("name", "testQueue");
    when(presetQueue3.toJSON()).thenReturn(json3);
  }
  
  @Test
  public void testAddPresetQueue() {  
    presetQueueController.addPresetQueue(presetQueue1);
    ArrayList<PresetQueue> expected = new ArrayList<PresetQueue>();
    expected.add(presetQueue1);
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
