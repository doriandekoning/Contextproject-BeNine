package com.benine.backend.camera;

import com.benine.backend.Preset;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created on 5-5-16.
 */
public class SimpleCameraTest {

  @Test
  public void testGetSetId() {
    Camera simpleCamera = new SimpleCamera();
    simpleCamera.setId(5665);
    Assert.assertEquals(5665, simpleCamera.getId());
  }
  
  @Test
  public void testGetSetMacAddress() throws CameraConnectionException {
    SimpleCamera simpleCamera = new SimpleCamera();
    simpleCamera.setMacAddress("testAddress");
    Assert.assertEquals("testAddress", simpleCamera.getMacAddress());
  }

  @Test
  public void testDefaultId() {
    Assert.assertEquals(-1, new SimpleCamera().getId());
  }

  @Test
  public void testGetSetStreamLink() {
    SimpleCamera simpleCamera = new SimpleCamera();
    simpleCamera.setStreamLink("link.something");
    Assert.assertEquals("link.something", simpleCamera.getStreamLink());
  }

  @Test
  public void testToJSON() throws Exception {
    SimpleCamera simpleCamera = new SimpleCamera();
    simpleCamera.setId(3);
    simpleCamera.setStreamLink("something");
    JSONObject actualJSON = new JSONObject(simpleCamera.toJSON());
    JSONObject expectedJSON = new JSONObject();
    expectedJSON.put("streamlink", "something");
    expectedJSON.put("id", 3);
    Assert.assertEquals(expectedJSON.get("streamlink"), actualJSON.get("streamlink"));
    Assert.assertEquals(expectedJSON.get("id"), actualJSON.get("id"));
  }
  
  @Test
  public void testEqualsId() {
    SimpleCamera camera1 = new SimpleCamera();
    camera1.setId(1);
    SimpleCamera camera2 = new SimpleCamera();
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public void testEqualsStreamlink() {
    SimpleCamera camera1 = new SimpleCamera();
    camera1.setStreamLink("test");
    SimpleCamera camera2 = new SimpleCamera();
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public void testEqualsNull() {
    SimpleCamera camera1 = new SimpleCamera();
    camera1.setStreamLink("test");
    assertNotEquals(camera1, null);
  }
  
  @Test
  public void testEqualsOtherObject() {
    SimpleCamera camera1 = new SimpleCamera();
    camera1.setStreamLink("test");
    String camera2 = "test";
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public void testEqualsPreset() {
    SimpleCamera camera1 = new SimpleCamera();
    Preset[] presets = {};
    camera1.setPresets(presets);
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setPresets(presets);
    camera2.setStreamLink("test");
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public void testEqualsNotPreset() {
    SimpleCamera camera1 = new SimpleCamera();
    Preset[] presets = {};
    camera1.setPresets(presets);
    SimpleCamera camera2 = new SimpleCamera();
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public void testEqual() {
    SimpleCamera camera1 = new SimpleCamera();
    camera1.setStreamLink("test");
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setStreamLink("test");
    assertEquals(camera1, camera2);
  }
  
  @Test
  public void testHashcode() {
    SimpleCamera camera1 = new SimpleCamera();
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setStreamLink("test");
    assertNotEquals(camera1.hashCode(), camera2.hashCode());
  }
  
  @Test
  public void testEqualsMACAddress() {
    SimpleCamera camera1 = new SimpleCamera();
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setMacAddress("test1");
    camera2.setMacAddress("test2");
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public void testHashcodeEquals() {
    SimpleCamera camera1 = new SimpleCamera();
    SimpleCamera camera2 = new SimpleCamera();
    assertEquals(camera1.hashCode(), camera2.hashCode());
  }
  
}

