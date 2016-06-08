package com.benine.backend.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.preset.Preset;

public class PresetQueueTest {

  
  Preset preset;
  Preset preset2;
  PresetQueue queue;
  PresetQueue queue2;
  ArrayList<Preset> list;
  ArrayList<Preset> list2;
  
  @Before
  public void setup() {
    preset = mock(Preset.class); 
    preset2 = mock(Preset.class);
    list = new ArrayList<Preset>();
    list.add(preset);
    list2= new ArrayList<Preset>();
    list2.add(preset);
    queue = new PresetQueue(1, "name", list);
    queue2 = new PresetQueue(1, "name", list2);
  }
  
  @Test
  public void testGetID() {
    assertEquals(1, queue.getID());
    
  }
  
  @Test
  public void testSetID() {
    queue.setID(2);
    assertEquals(queue.getID(), 2);
    
  }
  
  @Test
  public void testGetName() {
   assertEquals("name", queue.getName());
  }
  
  @Test
  public void testSetName() {
    queue.setName("test");
    assertEquals("test", queue.getName());
  }
  
  @Test
  public void testAddPreset() {
    queue.addPresetEnd(preset);
    assertEquals(2, queue.getQueue().size());
  } 
  
  @Test
  public void testDeletePreset() {
    queue.deletePreset(0);
    assertEquals(0, queue.getQueue().size());
    
  }
  
  @Test 
  public void testUpdatePreset() {
    queue.update(0, preset2);
    
  }
  
  @Test
  public void testInsertPreset() {
    queue.insertPreset(0, preset2);
    assertEquals(queue.getQueue().size(),2);
  }
  
   @Test
   public void testHashCode() {
     assertEquals(queue.hashCode(), queue2.hashCode());
       
   }
   
   @Test
   public void testToJSON() {
     JSONObject jsonObject = queue.toJSON();
     Assert.assertEquals(1, jsonObject.get("id"));
       
   }
   
   @Test
   public void testEquals() {
     assertEquals(queue,queue);
     assertEquals(queue,queue2);
     assertNotEquals(queue,null);
     assertEquals(queue.getName(),queue2.getName());
     assertNotEquals(queue, 2);
     
     queue2.setID(3);
     assertNotEquals(queue.getID(), queue2.getID());
     
   }
}
