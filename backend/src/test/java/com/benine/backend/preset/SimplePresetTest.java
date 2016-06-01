package com.benine.backend.preset;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

public class SimplePresetTest {
  
  SimplePreset preset;
  
  @Before
  public void setup() {
    ArrayList<String> keywords = new ArrayList<String>();
    keywords.add("foo");
    preset = new SimplePreset(1, keywords);
    preset.setId(1);
    preset.setImage("test");
  }
  
  @Test
  public void testGetID() {
    Assert.assertEquals(1, preset.getId());
  }
  
  @Test
  public void testToJSON() throws JSONException {
    JSONObject jsonObject = preset.toJSON();
    Assert.assertEquals(1, jsonObject.get("id"));
    Assert.assertEquals("test", jsonObject.get("image"));
    JSONArray expectedtagsJSON = new JSONArray();
    expectedtagsJSON.add("foo");
    Assert.assertEquals(expectedtagsJSON, jsonObject.get("tags"));
  }
  
  @Test
  public void testGetSQLQuery(){
    Assert.assertEquals("INSERT INTO presetsdatabase.simplepresets VALUES(1,'test',1)", preset.createAddSqlQuery());
  }

}
