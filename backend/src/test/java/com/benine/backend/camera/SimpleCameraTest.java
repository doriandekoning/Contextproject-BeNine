package com.benine.backend.camera;

import com.benine.backend.Preset;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dorian on 5-5-16.
 */
public class SimpleCameraTest {

  @Test
  public void testGetSetId() {
    Camera simpleCamera = new SimpleCamera();
    simpleCamera.setId(5665);
    Assert.assertEquals(5665, simpleCamera.getId());
  }

  @Test
  public void testDefaultId() {
    Assert.assertEquals(-1, new SimpleCamera().getId());
  }

  @Test
  public void testSetPresets() {
    Camera simpleCamera = new SimpleCamera();

    Preset[] presets = new Preset[16];
    presets[14] = new Preset(new Position(0,0), 0,0,0,false,0,0,false);


    simpleCamera.setPresets(presets);
    Assert.assertTrue(Arrays.equals(presets, simpleCamera.getPresets()));
  }

  @Test
  public void testSetPresetsFromArrayList() {
    Camera simpleCamera = new SimpleCamera();

    Preset[] presets = new Preset[16];
    presets[14] = new Preset(new Position(0,0), 0,0,0,false,0,0,false);
    ArrayList<Preset> presetarrayList = new ArrayList(Arrays.asList(presets));

    simpleCamera.setPresetsFromArrayList(presetarrayList);
    Assert.assertTrue(Arrays.equals(presets, simpleCamera.getPresets()));
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
}

