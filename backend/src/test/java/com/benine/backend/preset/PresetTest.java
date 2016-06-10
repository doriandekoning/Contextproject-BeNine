package com.benine.backend.preset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class for testing presets
 */
public abstract class PresetTest {

  @Before
  public void initialize() {

  }


  @Test
  public final void testSetImage() {
    Preset preset = getPreset();
    preset.setImage("static/test");
    Assert.assertEquals("static/test", preset.getImage());
  }

  @Test
  public void testSetCameraId() {
    Preset preset = getPreset();
    // Actual method to test
    preset.setCameraId(42);
    Assert.assertEquals(42, preset.getCameraId());
  }



  @Test
  public void testAddTag() {
    Preset preset = getPreset();

    preset.addTag("Violin");
    preset.addTag("Piano");
    List<String> keyWords = new ArrayList<String>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset.getTags());
  }
  @Test
  public void testEqualsEqualTags() {
    Preset preset1 = getPreset();
    Preset preset2 = getPreset();

    preset1.addTag("Violin");
    preset2.addTag("Violin");
    Assert.assertEquals(preset1, preset2);
  }

  @Test
  public void testEqualsNotEqualTags() {
    Preset preset1 = getPreset();
    Preset preset2 = getPreset();

    preset1.addTag("Violin");
    preset1.addTag("Piano");
    Assert.assertNotEquals(preset1, preset2);
  }

  @Test
  public void testAddTagList() {
    Preset preset1 = getPreset();
    preset1.addTag("Overview");
    Set<String> keyWords = new HashSet<>();
    keyWords.add("Violin");
    keyWords.add("Piano");
    preset1.addTags(keyWords);
    keyWords.add("Overview");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testRemoveTag() {
    Preset preset1 = getPreset();
    preset1.addTag("Violin");
    preset1.addTag(("Piano"));
    preset1.removeTag("Violin");
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Piano");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testDuplicateKeyWords() {
    Preset preset1 = getPreset();
    preset1.addTag("Violin");
    preset1.addTag(("Violin"));
    ArrayList<String>  keyWords = new ArrayList<>();
    keyWords.add("Violin");
    Assert.assertEquals(new HashSet<String>(keyWords), preset1.getTags());
  }

  @Test
  public void testSetId() {
    Preset preset = getPreset();
    preset.setId(1);
    Assert.assertEquals(1, preset.getId());
  }


  @Test
  public void testEqualsSamePreset() {
    Preset preset = getPreset();
    Preset preset2 = getPreset();
    Assert.assertEquals(preset, preset2);
  }


  @Test
  public void testEqualsDifferentCameraId() {
    Preset preset = getPreset();
    Preset preset2 = getPreset();
    preset2.setCameraId(preset.getCameraId()+1);
    Assert.assertNotEquals(preset, preset2);
  }


  @Test
  public void testEqualsOtherPresetID() {
    Preset preset = getPreset();
    Preset preset2 = getPreset();
    preset2.setId(5);
    Assert.assertNotEquals(preset, preset2);
  }

  @Test
  public void testEqualsOtherObject() {
    Preset preset = getPreset();
    Assert.assertNotEquals(preset, 1);
  }

  @Test
  public void testEqualsSameObject() {
    Preset preset = getPreset();

    Assert.assertEquals(preset, preset);
  }

  @Test
  public void testEqualsNull() {
    Preset preset = getPreset();
    Assert.assertNotEquals(preset, null);
  }



  public abstract Preset getPreset();



}
