package com.benine.backend;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.benine.backend.camera.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dorian on 3-5-16.
 */
public class PresetTest {

  @Test
  public void testToJSON() throws JSONException {
    ArrayList<String> keywords = new ArrayList<String>();
    keywords.add("foo");
    Preset preset = new Preset(new Position(10, 12), 13, 40, 56, true, 1, 2, false, 0, keywords);
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
    Assert.assertEquals("foo", jsonObject.getJSONArray("tags").get(0));
  }

  @Test
  public void testGetMethods() {
    Preset preset = new Preset(new Position(1, 2), 3, 4, 5, true, 1, 2, false, 0);
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
  public void testSetPosition() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setPosition(new Position(1, 2));   
    Position expected = new Position(1, 2);
    Assert.assertEquals(expected, preset.getPosition());
  }
  
  @Test
  public void testSetPanSpeed() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setPanspeed(20);
    Assert.assertEquals(20, preset.getPanspeed());
  }
  
  @Test
  public void testSetTiltSpeed() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setTiltspeed(20);
    Assert.assertEquals(20, preset.getTiltspeed());
  }
  
  @Test
  public void testSetAutoFocus() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setAutofocus(true);
    Assert.assertEquals(true, preset.isAutofocus());
  }
  
  @Test
  public void testSetAutoIris() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setAutoiris(true);
    Assert.assertEquals(true, preset.isAutoiris());
  }
  
  @Test
  public void testSetIris() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setIris(60);
    Assert.assertEquals(60, preset.getIris());
  }
  
  @Test
  public void testSetFocus() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setFocus(55);
    Assert.assertEquals(55, preset.getFocus());
  }
  
  @Test
  public void testSetZoom() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setZoom(50);
    Assert.assertEquals(50, preset.getZoom());
  }
  
  @Test
  public void testSetId() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setId(1);
    Assert.assertEquals(1, preset.getId());
  }
  
  @Test
  public void testSetImage() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.setImage("static/test");
    Assert.assertEquals("static/test", preset.getImage());
  }
  
  @Test
  public void testHashCodefalse() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Assert.assertEquals(preset.hashCode(), preset2.hashCode());
  }
  
  @Test
  public void testHashCodetrue() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Assert.assertNotEquals(preset.hashCode(), preset2.hashCode());
  }
  
  @Test
  public void testEqualsSameObject() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Assert.assertEquals(preset, preset);
  }
  
  @Test
  public void testEqualsNull() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Assert.assertNotEquals(preset, null);
  }
  
  @Test
  public void testEqualsOtherObject() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Assert.assertNotEquals(preset, 1);
  }
  
  @Test
  public void testEqualsOtherPresetID() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    preset2.setId(5);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPosition() {
    Preset preset = new Preset(new Position(1, 0), 0, 0, 0, true, 1, 2, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherZoom() {
    Preset preset = new Preset(new Position(0, 0), 1, 0, 0, true, 1, 2, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherFocus() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 6, 0, true, 1, 2, true, 0);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherIris() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 8, true, 1, 2, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPanspeed() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, true, 4, 2, true, 0);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherTiltspeed() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 9, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherAutoFocus() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, true, 0);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherAutoIris() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, true, 0);
    Assert.assertNotEquals(preset, preset2);
  }

  @Test
  public void testEqualsDifferentCameraId() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, true, 3);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsSamePreset() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Assert.assertEquals(preset, preset2);
  }

  @Test
  public void testAddTag() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset.addTag("Violin");
    preset.addTag("Piano");
    List<String> keyWords = new ArrayList<String>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset.getTags());
  }
  @Test
  public void testEqualsEqualTags() {
    Preset preset1 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset1.addTag("Violin");
    preset2.addTag("Violin");
    Assert.assertEquals(preset1, preset2);
  }
  @Test
  public void testEqualsNotEqualTags() {
    Preset preset1 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    Preset preset2 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0 );
    preset1.addTag("Violin");
    preset1.addTag("Piano");
    Assert.assertNotEquals(preset1, preset2);
  }
  @Test
  public void testAddTagList() {
    Preset preset1 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset1.addTag("Overview");
    List<String> keyWords = new ArrayList<String>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    preset1.addTags(keyWords);
    keyWords.add("Overview");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }
  @Test
  public void testTagsConstructor() {
    List<String> keyWords = new ArrayList<String>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    Preset preset1 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, keyWords);
    keyWords.add("Overview");
    preset1.addTag("Overview");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }
  @Test
  public void testRemoveTag() {
    Preset preset1 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset1.addTag("Violin");
    preset1.addTag(("Piano"));
    preset1.removeTag("Violin");
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }
  @Test
  public void testDuplicateKeyWords() {
    Preset preset1 = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0);
    preset1.addTag("Violin");
    preset1.addTag(("Violin"));
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Violin");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testAddKeyWord() {
    Preset.addKeyword("foo");
    // Since this is static and we don't know what other test cases will do
    // we'll just check if it contains foo instead of comparing it to an expected set
    Assert.assertTrue(Preset.getKeywords().contains("foo"));
  }

  @Test
  public void testRemoveKeyWord() {
    Preset.addKeyword("bar");
    Preset.removeKeyword("bar");
    Assert.assertFalse(Preset.getKeywords().contains("bar"));
  }

  @Test
  public void testGetCameraId() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 42);
    Assert.assertEquals(42, preset.getCameraId());
  }

  @Test
  public void testSetCameraId() {
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, -1);
    // Actual method to test
    preset.setCameraId(42);
    Assert.assertEquals(42, preset.getCameraId());
  }
}