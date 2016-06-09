package com.benine.backend.preset;

import com.benine.backend.camera.*;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created on 3-5-16.
 */
public class IPCameraPresetTest extends PresetTest {
  
  IPCameraPreset preset;
  
  @Before
  public void setup() {
    Set<String> keywords = new HashSet<>();
    keywords.add("foo");
    preset = new IPCameraPreset(new ZoomPosition(10, 12, 13), 40, 56, true, false, 0);
    preset.addTags(keywords);
  }
  
  @Test
  public void testExcecutePresetMoveTo() throws CameraConnectionException, CameraBusyException {
    IPCamera camera = mock(IPCamera.class);
    preset.excecutePreset(camera);
    verify(camera).moveTo(new Position(10, 12), 15, 1);
  }
  
  @Test(expected = CameraConnectionException.class)
  public void testExcecutePresetException() throws CameraConnectionException, CameraBusyException {
    SimpleCamera camera = mock(SimpleCamera.class);
    preset.excecutePreset(camera);
  }
  
  @Test
  public void testCreateSQL() throws CameraConnectionException {
    Assert.assertEquals("INSERT INTO presetsdatabase.presets VALUES(-1,10.0,12.0,13,40,56,1,15,1,0,'null',0)", preset.createAddSqlQuery());
  }

  @Test
  public void testToJSON() throws JSONException {
    ArrayList<String> keywords = new ArrayList<String>();
    keywords.add("foo");
    IPCameraPreset preset = getPreset();
    preset.addTags(keywords);

    JSONArray tagsArray = new JSONArray();
    keywords.forEach(k -> tagsArray.add(k));

    JSONObject jsonObject = preset.toJSON();

    Assert.assertEquals(preset.getPosition().getPan(), jsonObject.get("pan"));
    Assert.assertEquals(preset.getPosition().getTilt(), jsonObject.get("tilt"));
    Assert.assertEquals(preset.getPosition().getZoom(), jsonObject.get("zoom"));
    Assert.assertEquals(preset.getFocus(), jsonObject.get("focus"));
    Assert.assertEquals(preset.getIris(), jsonObject.get("iris"));
    Assert.assertEquals(preset.isAutofocus(), jsonObject.get("autofocus"));
    Assert.assertEquals(preset.getPanspeed(), jsonObject.get("panspeed"));
    Assert.assertEquals(preset.getTiltspeed(), jsonObject.get("tiltspeed"));
    Assert.assertEquals(preset.isAutoiris(), jsonObject.get("autoiris"));
    Assert.assertEquals(tagsArray, jsonObject.get("tags"));
  }

  @Test
  public void testIsAutoiris() {
    IPCameraPreset preset = getPreset();
    preset.setAutoiris(true);
    Assert.assertEquals(true, preset.isAutoiris());
  }

  @Test
  public void testSetPosition() {
    IPCameraPreset preset = getPreset();
    preset.setPosition(new ZoomPosition(1, 2, 3));
    ZoomPosition expected = new ZoomPosition(1, 2, 3);
    Assert.assertEquals(expected, preset.getPosition());
  }
  
  @Test
  public void testSetPanSpeed() {
    IPCameraPreset preset = getPreset();
    preset.setPanspeed(20);
    Assert.assertEquals(20, preset.getPanspeed());
  }
  
  @Test
  public void testSetTiltSpeed() {
    IPCameraPreset preset = getPreset();
    preset.setTiltspeed(20);
    Assert.assertEquals(20, preset.getTiltspeed());
  }
  
  @Test
  public void testSetAutoFocus() {
    IPCameraPreset preset = getPreset();
    preset.setAutofocus(true);
    Assert.assertEquals(true, preset.isAutofocus());
  }
  
  @Test
  public void testSetAutoIris() {
    IPCameraPreset preset = getPreset();
    preset.setAutoiris(true);
    Assert.assertEquals(true, preset.isAutoiris());
  }
  
  @Test
  public void testSetIris() {
    IPCameraPreset preset = getPreset();
    preset.setIris(60);
    Assert.assertEquals(60, preset.getIris());
  }
  
  @Test
  public void testSetFocus() {
    IPCameraPreset preset = getPreset();
    preset.setFocus(55);
    Assert.assertEquals(55, preset.getFocus());
  }
  
  @Test
  public void testHashCode() {
    Preset preset = getPreset();
    Preset preset2 = getPreset();
    Assert.assertEquals(preset.hashCode(), preset2.hashCode());
  }


  

  @Test
  public void testEqualsOtherPosition() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setPosition(new ZoomPosition(0, 0, 0));
    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherFocus() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setFocus(12);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherIris() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setIris(preset.getIris()+1);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherPanspeed() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setPanspeed(preset.getPanspeed()+1);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherTiltspeed() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setTiltspeed(preset.getTiltspeed()+1);

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherAutoFocus() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setAutofocus(!preset.isAutofocus());

    Assert.assertNotEquals(preset, preset2);
  }
  
  @Test
  public void testEqualsOtherAutoIris() {
    IPCameraPreset preset = getPreset();
    IPCameraPreset preset2 = getPreset();
    preset2.setAutoiris(!preset.isAutoiris());
    Assert.assertNotEquals(preset, preset2);
  }

  


  public final IPCameraPreset getPreset() {
    IPCameraPreset preset = new IPCameraPreset(new ZoomPosition(4.2, 42.42, 4), 2, 5, false, true, 34);
    return preset;
  }
}