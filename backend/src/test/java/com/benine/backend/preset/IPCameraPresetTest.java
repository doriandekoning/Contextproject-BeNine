package com.benine.backend.preset;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created on 3-5-16.
 */
public class IPCameraPresetTest {
  
  IPCameraPreset preset;
  
  @Before
  public void setup() {
    Set<String> keywords = new HashSet<>();
    keywords.add("foo");
    preset = new IPCameraPreset(new Position(10, 12), 13, 40, 56, true, 1, 2, false, 0, keywords, "name");
  }
  
  @Test
  public void testExcecutePresetMoveTo() throws CameraConnectionException {
    IPCamera camera = mock(IPCamera.class);
    preset.excecutePreset(camera);
    verify(camera).moveTo(new Position(10, 12), 1, 2);
  }
  
  @Test(expected = CameraConnectionException.class)
  public void testExcecutePresetException() throws CameraConnectionException {
    SimpleCamera camera = mock(SimpleCamera.class);
    preset.excecutePreset(camera);
  }
  
  @Test
  public void testCreateSQL() throws CameraConnectionException {
    Assert.assertEquals("INSERT INTO presetsdatabase.IPpreset VALUES(-1,10.0,12.0,13,40,56,1,1,2,0,'null',0)", preset.createAddSqlQuery());
  }

  @Test
  public void testDeleteSQL() {
    Assert.assertEquals("DELETE FROM IPpreset WHERE ID = -1", preset.createDeleteSQL());
  }

  @Test
  public void testToJSON() throws JSONException {
    JSONObject jsonObject = preset.toJSON();
    Assert.assertEquals(10.0, jsonObject.get("pan"));
    Assert.assertEquals(12.0, jsonObject.get("tilt"));
    Assert.assertEquals(13, jsonObject.get("zoom"));
    Assert.assertEquals(40, jsonObject.get("focus"));
    Assert.assertEquals(56, jsonObject.get("iris"));
    Assert.assertEquals(true, jsonObject.get("autofocus"));
    Assert.assertEquals(1, jsonObject.get("panspeed"));
    Assert.assertEquals(2, jsonObject.get("tiltspeed"));
    Assert.assertEquals(false, jsonObject.get("autoiris"));
    JSONArray expectedtagsJSON = new JSONArray();
    expectedtagsJSON.add("foo");
    Assert.assertEquals(expectedtagsJSON, jsonObject.get("tags"));
  }

  @Test
  public void testGetMethods() {
    Assert.assertEquals(new Position(10, 12), preset.getPosition());
    Assert.assertEquals(13, preset.getZoom());
    Assert.assertEquals(40, preset.getFocus());
    Assert.assertEquals(56, preset.getIris());
    Assert.assertEquals(true, preset.isAutofocus());
    Assert.assertEquals(1, preset.getPanspeed());
    Assert.assertEquals(2, preset.getTiltspeed());
    Assert.assertEquals(false, preset.isAutoiris());
    Assert.assertEquals("name", preset.getName());
  }

  @Test
  public void testSetPosition() {
    preset.setPosition(new Position(1, 2));   
    Position expected = new Position(1, 2);
    Assert.assertEquals(expected, preset.getPosition());
  }
  
  @Test
  public void testSetPanSpeed() {
    preset.setPanspeed(20);
    Assert.assertEquals(20, preset.getPanspeed());
  }
  
  @Test
  public void testSetTiltSpeed() {
    preset.setTiltspeed(20);
    Assert.assertEquals(20, preset.getTiltspeed());
  }
  
  @Test
  public void testSetAutoFocus() {
    preset.setAutofocus(true);
    Assert.assertEquals(true, preset.isAutofocus());
  }
  
  @Test
  public void testSetAutoIris() {
    preset.setAutoiris(true);
    Assert.assertEquals(true, preset.isAutoiris());
  }
  
  @Test
  public void testSetIris() {
    preset.setIris(60);
    Assert.assertEquals(60, preset.getIris());
  }
  
  @Test
  public void testSetFocus() {
    preset.setFocus(55);
    Assert.assertEquals(55, preset.getFocus());
  }
  
  @Test
  public void testSetZoom() {
    preset.setZoom(50);
    Assert.assertEquals(50, preset.getZoom());
  }
  
  @Test
  public void testSetId() {
    preset.setId(1);
    Assert.assertEquals(1, preset.getId());
  }
  
  @Test
  public void testSetImage() {
    preset.setImage("static/test");
    Assert.assertEquals("static/test", preset.getImage());
  }
  
  @Test
  public void testHashCodefalse() {
    Set<String> keywords = new HashSet<>();
    keywords.add("foo");
    Preset preset2 = new IPCameraPreset(new Position(10, 12), 13, 40, 56, true, 1, 2, false, 0, keywords, "name");
    Assert.assertEquals(preset.hashCode(), preset2.hashCode());
  }
  
  @Test
  public void testHashCodetrue() {
    Preset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    Assert.assertNotEquals(preset.hashCode(), preset2.hashCode());
  }
  
  @Test
  public void testEqualsSameObject() {
    Assert.assertEquals(preset, preset);
  }
  
  @Test
  public void testEqualsNull() {
    Assert.assertNotEquals(preset, null);
  }
  
  @Test
  public void testEqualsOtherObject() {
    Assert.assertNotEquals(preset, 1);
  }
  
  @Test
  public void testEqualsOtherPresetID() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    preset2.setId(5);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPosition() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherZoom() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherFocus() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherIris() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPanspeed() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherTiltspeed() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherAutoFocus() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherAutoIris() {
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, true, 1, 2, true, 0, "name");
    Assert.assertNotEquals(preset, preset2);
  }

  @Test
  public void testEqualsDifferentCameraId() {
    IPCameraPreset preset = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 1, "name");
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsSamePreset() {
    IPCameraPreset preset = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    Assert.assertEquals(preset, preset2);
  }

  @Test
  public void testEqualsOtherName() {
    IPCameraPreset preset = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name2");
    Assert.assertNotEquals(preset, preset2);
  }

  @Test
  public void testAddTag() {
    IPCameraPreset preset = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    preset.addTag("Violin");
    preset.addTag("Piano");
    List<String> keyWords = new ArrayList<String>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset.getTags());
  }
  @Test
  public void testEqualsEqualTags() {
    IPCameraPreset preset1 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    preset1.addTag("Violin");
    preset2.addTag("Violin");
    Assert.assertEquals(preset1, preset2);
  }
  @Test
  public void testEqualsNotEqualTags() {
    IPCameraPreset preset1 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    preset1.addTag("Violin");
    preset1.addTag("Piano");
    Assert.assertNotEquals(preset1, preset2);
  }
  @Test
  public void testAddTagList() {
    IPCameraPreset preset1 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    preset1.addTag("Overview");
    Set<String> keyWords = new HashSet<>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    preset1.addTags(keyWords);
    keyWords.add("Overview");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }
  @Test
  public void testTagsConstructor() {
    Set<String> keyWords = new HashSet<String>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    IPCameraPreset preset1 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, keyWords, "name");
    keyWords.add("Overview");
    preset1.addTag("Overview");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }
  @Test
  public void testRemoveTag() {
    IPCameraPreset preset1 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    preset1.addTag("Violin");
    preset1.addTag(("Piano"));
    preset1.removeTag("Violin");
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }
  @Test
  public void testDuplicateKeyWords() {
    IPCameraPreset preset1 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, "name");
    preset1.addTag("Violin");
    preset1.addTag(("Violin"));
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Violin");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testSetCameraId() {
    IPCameraPreset preset = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, -1, "name");
    // Actual method to test
    preset.setCameraId(42);
    Assert.assertEquals(42, preset.getCameraId());
  }

  @Test
  public void testSetName() {
    preset.setName("name2");
    Assert.assertEquals("name2", preset.getName());
  }
}