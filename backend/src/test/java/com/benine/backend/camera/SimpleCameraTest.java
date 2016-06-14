package com.benine.backend.camera;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.video.StreamType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.json.simple.JSONObject;

/**
 * Created on 5-5-16.
 */
public class SimpleCameraTest extends BasicCameraTest {
  
  SimpleCamera simpleCamera;
  
  @Override
  public BasicCamera getCamera() {
    return new SimpleCamera();
  }
  
  @Before
  public void setup() {
    simpleCamera = new SimpleCamera();
  }
  
  @Test
  public void testGetSetMacAddress() throws CameraConnectionException {
    simpleCamera.setMacAddress("testAddress");
    Assert.assertEquals("testAddress", simpleCamera.getMacAddress());
  }

  @Test
  public void testGetSetStreamLink() {
    simpleCamera.setStreamLink("link.something");
    Assert.assertEquals("link.something", simpleCamera.getStreamLink());
  }

  @Test
  public void testToJSON() throws Exception {
    simpleCamera.setId(3);
    simpleCamera.setStreamLink("something");
    JSONObject expectedJSON = new JSONObject();
    expectedJSON.put("id", 3);
    expectedJSON.put("inuse", false);
    Assert.assertEquals(expectedJSON, simpleCamera.toJSON());
  }
  
  @Test
  public void testEqualsId() {
    simpleCamera.setId(1);
    SimpleCamera camera2 = new SimpleCamera();
    assertNotEquals(simpleCamera, camera2);
  }
  
  @Test
  public void testEqualsStreamlink() {
    simpleCamera.setStreamLink("test");
    SimpleCamera camera2 = new SimpleCamera();
    assertNotEquals(simpleCamera, camera2);
  }
  
  @Test
  public void testEqualsNull() {
    simpleCamera.setStreamLink("test");
    assertNotEquals(simpleCamera, null);
  }
  
  @Test
  public void testEqualsOtherObject() {
    simpleCamera.setStreamLink("test");
    String camera2 = "test";
    assertNotEquals(simpleCamera, camera2);
  }
  
  @Test
  public void testEqual() {
    simpleCamera.setStreamLink("test");
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setStreamLink("test");
    assertEquals(simpleCamera, camera2);
  }
  
  @Test
  public void testHashcode() {
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setStreamLink("test");
    assertNotEquals(simpleCamera.hashCode(), camera2.hashCode());
  }
  
  @Test
  public void testEqualsMACAddress() {
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setMacAddress("test1");
    camera2.setMacAddress("test2");
    assertNotEquals(simpleCamera, camera2);
  }
  
  @Test
  public void testHashcodeEquals() {
    SimpleCamera camera2 = new SimpleCamera();
    assertEquals(simpleCamera.hashCode(), camera2.hashCode());
  }
  
  @Test
  public void testGetStreamType() {
    assertEquals(StreamType.MJPEG, simpleCamera.getStreamType());
  }
}

