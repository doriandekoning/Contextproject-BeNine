package com.benine.backend.preset;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    preset = new IPCameraPreset(new Position(10, 12), 13, 40, 56, true, 1, 2, false, 0, keywords);
  }
  
  @Test
  public void testExcecutePresetMoveTo() throws CameraConnectionException, CameraBusyException {
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
    Assert.assertEquals("INSERT INTO presetsdatabase.presets VALUES(-1,10.0,12.0,13,40,56,1,1,2,0,'null',0)", preset.createAddSqlQuery());
  }

  @Test
  public void testToJSON() throws JSONException {
    ArrayList<String> keywords = new ArrayList<String>();
    keywords.add("foo");
    Preset preset = new IPCameraPresetFactory().createPreset(new Position(10, 12), 13, 40, 56, true, 1, 2, false, 0, keywords);

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
    Preset preset = getDefaultPreset();
    Assert.assertEquals(new Position(4.2, 42.42), preset.getPosition());
    Assert.assertEquals(4, preset.getZoom());
    Assert.assertEquals(2, preset.getFocus());
    Assert.assertEquals(5, preset.getIris());
    Assert.assertEquals(false, preset.isAutofocus());
    Assert.assertEquals(1, preset.getPanspeed());
    Assert.assertEquals(2, preset.getTiltspeed());
    Assert.assertEquals(true, preset.isAutoiris());
  }

  @Test
  public void testSetPosition() {
    Preset preset = getDefaultPreset();
    preset.setPosition(new Position(1, 2));   
    Position expected = new Position(1, 2);
    Assert.assertEquals(expected, preset.getPosition());
  }
  
  @Test
  public void testSetPanSpeed() {
    Preset preset = getDefaultPreset();
    preset.setPanspeed(20);
    Assert.assertEquals(20, preset.getPanspeed());
  }
  
  @Test
  public void testSetTiltSpeed() {
    Preset preset = getDefaultPreset();
    preset.setTiltspeed(20);
    Assert.assertEquals(20, preset.getTiltspeed());
  }
  
  @Test
  public void testSetAutoFocus() {
    Preset preset = getDefaultPreset();
    preset.setAutofocus(true);
    Assert.assertEquals(true, preset.isAutofocus());
  }
  
  @Test
  public void testSetAutoIris() {
    Preset preset = getDefaultPreset();
    preset.setAutoiris(true);
    Assert.assertEquals(true, preset.isAutoiris());
  }
  
  @Test
  public void testSetIris() {
    Preset preset = getDefaultPreset();
    preset.setIris(60);
    Assert.assertEquals(60, preset.getIris());
  }
  
  @Test
  public void testSetFocus() {
    Preset preset = getDefaultPreset();
    preset.setFocus(55);
    Assert.assertEquals(55, preset.getFocus());
  }
  
  @Test
  public void testSetZoom() {
    Preset preset = getDefaultPreset();

    preset.setZoom(50);
    Assert.assertEquals(50, preset.getZoom());
  }
  
  @Test
  public void testSetId() {
    Preset preset = getDefaultPreset();
    preset.setId(1);
    Assert.assertEquals(1, preset.getId());
  }
  
  @Test
  public void testSetImage() {
    Preset preset = getDefaultPreset();
    preset.setImage("static/test");
    Assert.assertEquals("static/test", preset.getImage());
  }
  
  @Test
  public void testHashCodetrue() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    Assert.assertEquals(preset.hashCode(), preset2.hashCode());
  }

  @Test
  public void testEqualsSameObject() {
    Preset preset = getDefaultPreset();

    Assert.assertEquals(preset, preset);
  }
  
  @Test
  public void testEqualsNull() {
    Preset preset = getDefaultPreset();
    Assert.assertNotEquals(preset, null);
  }
  
  @Test
  public void testEqualsOtherObject() {
    Preset preset = getDefaultPreset();
    Assert.assertNotEquals(preset, 1);
  }
  
  @Test
  public void testEqualsOtherPresetID() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setId(5);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPosition() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setPosition(new Position(0, 0));
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherZoom() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setZoom(34);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherFocus() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setFocus(12);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherIris() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setIris(preset.getIris()+1);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPanspeed() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setPanspeed(preset.getPanspeed()+1);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherTiltspeed() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setTiltspeed(preset.getTiltspeed()+1);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherAutoFocus() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setAutofocus(!preset.isAutofocus());

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherAutoIris() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setAutoiris(!preset.isAutoiris());
    Assert.assertNotEquals(preset, preset2);
  }

  @Test
  public void testEqualsDifferentCameraId() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    preset2.setCameraId(preset.getCameraId()+1);
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsSamePreset() {
    Preset preset = getDefaultPreset();
    Preset preset2 = getDefaultPreset();
    Assert.assertEquals(preset, preset2);
  }

  @Test
  public void testAddTag() {
    Preset preset = getDefaultPreset();

    preset.addTag("Violin");
    preset.addTag("Piano");
    List<String> keyWords = new ArrayList<String>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset.getTags());
  }
  @Test
  public void testEqualsEqualTags() {
    Preset preset1 = getDefaultPreset();
    Preset preset2 = getDefaultPreset();

    preset1.addTag("Violin");
    preset2.addTag("Violin");
    Assert.assertEquals(preset1, preset2);
  }

  @Test
  public void testEqualsNotEqualTags() {
    Preset preset1 = getDefaultPreset();
    Preset preset2 = getDefaultPreset();

    preset1.addTag("Violin");
    preset1.addTag("Piano");
    Assert.assertNotEquals(preset1, preset2);
  }

  @Test
  public void testAddTagList() {
    Preset preset1 = getDefaultPreset();
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
    IPCameraPreset preset1 = new IPCameraPreset(new Position(0, 0), 0, 0, 0, false, 1, 2, false, 0, keyWords);
    keyWords.add("Overview");
    preset1.addTag("Overview");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testRemoveTag() {
    Preset preset1 = getDefaultPreset();
    preset1.addTag("Violin");
    preset1.addTag(("Piano"));
    preset1.removeTag("Violin");
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testDuplicateKeyWords() {
    Preset preset1 = getDefaultPreset();
    preset1.addTag("Violin");
    preset1.addTag(("Violin"));
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Violin");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testSetCameraId() {
    Preset preset = getDefaultPreset();
    // Actual method to test
    preset.setCameraId(42);
    Assert.assertEquals(42, preset.getCameraId());
  }

  public final Preset getDefaultPreset() {
    Preset preset = new IPCameraPresetFactory().createPreset();
    preset.setCameraId(34);
    preset.setPosition(new Position(4.2, 42.42));
    preset.setZoom(4);
    preset.setFocus(2);
    preset.setIris(5);
    preset.setPanspeed(1);
    preset.setTiltspeed(2);
    preset.setAutoiris(true);
    preset.setAutofocus(false);
    return preset;
  }
}