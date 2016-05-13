package com.benine.backend;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.benine.backend.camera.Position;

/**
 * Created by dorian on 3-5-16.
 */
public class PresetTest {

  @Test
  public void testToJSON() throws JSONException {
    Preset preset = new Preset(new Position(10, 12), 13, 40, 56, true, 1, 2, false);
    String json = preset.toJSON();
    JSONObject jsonObject = new JSONObject(json);
    Assert.assertEquals(10.0, jsonObject.get("pan"));
    Assert.assertEquals(12.0, jsonObject.get("tilt"));
    Assert.assertEquals(13, jsonObject.get("zoom"));
    Assert.assertEquals(40, jsonObject.get("focus"));
    Assert.assertEquals(56, jsonObject.get("iris"));
    Assert.assertEquals(true, jsonObject.get("autofocus"));
    Assert.assertEquals(1, jsonObject.get("panspeed"));
    Assert.assertEquals(2, jsonObject.get("tiltspeed"));
    Assert.assertEquals(false, jsonObject.get("autoiris"));
  }

  @Test
  public void testGetMethods() {
    Preset preset = new Preset(new Position(1, 2), 3, 4, 5, true, 1, 2, false);
    Assert.assertEquals(new Position(1, 2), preset.getPosition());
    Assert.assertEquals(3, preset.getZoom());
    Assert.assertEquals(4, preset.getFocus());
    Assert.assertEquals(5, preset.getIris());
    Assert.assertEquals(true, preset.isAutofocus());
    Assert.assertEquals(1, preset.getPanspeed());
    Assert.assertEquals(2, preset.getTiltspeed());
    Assert.assertEquals(false, preset.isAutoiris());
  }

  @Test
  public void testSetMethods() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false);
    preset.setPosition(new Position(1, 2));
    preset.setZoom(3);
    preset.setFocus(4);
    preset.setIris(5);
    preset.setAutofocus(true);
    preset.setPanspeed(4);
    preset.setTiltSpeed(5);
    preset.setAutoiris(true);
    Position expected = new Position(1, 2);
    Assert.assertEquals(expected, preset.getPosition());
    Assert.assertEquals(3, preset.getZoom());
    Assert.assertEquals(4, preset.getFocus());
    Assert.assertEquals(5, preset.getIris());
    Assert.assertEquals(true, preset.isAutofocus());
    Assert.assertEquals(4, preset.getFocus());
    Assert.assertEquals(5, preset.getTiltspeed());
    Assert.assertEquals(true, preset.isAutoiris());
  }
}
