package com.benine.backend;

import com.benine.backend.database.DatabasePreset;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dorian on 3-5-16.
 */
public class PresetTest {

  @Test
  public void testToJSON() throws JSONException {
    DatabasePreset preset = new DatabasePreset(10, 12, 13, 40, 56, true);
    String json = preset.toJSON();
    JSONObject jsonObject = new JSONObject(json);
    Assert.assertEquals(10, jsonObject.get("pan"));
    Assert.assertEquals(12, jsonObject.get("tilt"));
    Assert.assertEquals(13, jsonObject.get("zoom"));
    Assert.assertEquals(40, jsonObject.get("focus"));
    Assert.assertEquals(56, jsonObject.get("iris"));
    Assert.assertEquals(true, jsonObject.get("autofocus"));
  }
}
