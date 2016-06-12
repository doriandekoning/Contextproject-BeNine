package com.benine.backend.preset;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;
import com.benine.backend.video.VideoFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Abstract class for testing presets
 */
public abstract class PresetTest {
  
  Preset preset;

  @Before
  public void initialize() {
    preset = getPreset();
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
    Preset preset2 = getPreset();

    preset.addTag("Violin");
    preset2.addTag("Violin");
    Assert.assertEquals(preset, preset2);
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
    preset.setId(1);
    Assert.assertEquals(1, preset.getId());
  }


  @Test
  public void testEqualsSamePreset() {
    Preset preset2 = getPreset();
    Assert.assertEquals(preset, preset2);
  }


  @Test
  public void testEqualsDifferentCameraId() {
    Preset preset2 = getPreset();
    preset2.setCameraId(preset.getCameraId()+1);
    Assert.assertNotEquals(preset, preset2);
  }


  @Test
  public void testEqualsOtherPresetID() {
    Preset preset2 = getPreset();
    preset2.setId(5);
    Assert.assertNotEquals(preset, preset2);
  }

  @Test
  public void testEqualsOtherObject() {
    Assert.assertNotEquals(preset, 1);
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
  public void testCreateImage() throws StreamNotAvailableException, IOException, SQLException{
    StreamReader streamReader = mock(StreamReader.class);
    VideoFrame videoFrame = mock(VideoFrame.class);
    byte[] image = IOUtils.toByteArray(new FileInputStream("resources" + File.separator + "test" + File.separator + "firstframe.jpg"));
    when(videoFrame.getImage()).thenReturn(image);
    when(videoFrame.getHeaderBytes()).thenReturn(image);
    when(streamReader.getSnapShot()).thenReturn(videoFrame);
    preset.createImage(streamReader, "resources" + File.separator + "test" + File.separator);
    File path = new File("resources" + File.separator + "test" + File.separator + preset.getImage());
    path.delete();
    assertEquals("preset_0.jpg", preset.getImage());

  }


  public abstract Preset getPreset();
}
