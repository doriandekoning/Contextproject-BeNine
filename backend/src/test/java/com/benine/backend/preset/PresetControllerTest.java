package com.benine.backend.preset;

import com.benine.backend.ServerController;
import com.benine.backend.database.MySQLDatabase;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Created on 18-5-16.
 */
public class PresetControllerTest {
  
  private PresetController presetController;
  private Preset preset;
  private Preset preset2;
  
  @Before
  public void setup() throws SQLException {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    ServerController.getInstance();
    ServerController.getInstance().getDatabaseController().setDatabase(mock(MySQLDatabase.class));
    presetController = new PresetController();
    preset = mock(Preset.class);
    preset2 = mock(Preset.class);
    when(preset.getId()).thenReturn(1);
    when(preset.getName()).thenReturn("");
    when(preset2.getName()).thenReturn("");
  }
  
  @Test
  public void testPresetJSON() throws SQLException {
    Preset preset = mock(Preset.class);
    when(preset.getName()).thenReturn("");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("image", "test");
    when(preset.toJSON()).thenReturn(jsonObject);
    presetController.addPreset(preset);
    String result = presetController.getPresetsJSON(null);
    jsonObject.put("image", "/static/presets/test");
    JSONObject expected = new JSONObject();
    JSONArray presetsJSON = new JSONArray();
    presetsJSON.add(jsonObject);
    expected.put("presets", presetsJSON);
    JSONArray tagsJSON = new JSONArray();
    expected.put("tags", tagsJSON);
    Assert.assertEquals(expected.toJSONString(), result);
  }
  

  @Test
  public void testAddPreset() throws Exception {   
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(preset);
    presetController.addPreset(preset);
    Assert.assertEquals(expectedPresets, presetController.getPresets());
  }
  
  @Test
  public void testAddPresetWithoutName() throws Exception {   
    presetController.addPreset(preset);
    verify(preset).setName("Preset 1");
  }
  
  @Test
  public void testAddPresetWithName() throws Exception {   
    when(preset.getName()).thenReturn("Preset name");
    presetController.addPreset(preset);
    verify(preset, never()).setName("Preset 1");
  }
  
  @Test
  public void testUpdatePreset() throws Exception {
    Preset newpreset = mock(Preset.class);
    when(newpreset.getId()).thenReturn(1);
    presetController.addPreset(preset);
    presetController.updatePreset(newpreset);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(newpreset);
    Assert.assertEquals(expectedPresets, presetController.getPresets());
  }
  
  @Test
  public void testAddPresetWithoutID() throws Exception {
    when(preset.getId()).thenReturn(-1);
    presetController.addPreset(preset);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(preset);
    Assert.assertEquals(expectedPresets, presetController.getPresets());
  }

  @Test
  public void testAddPresetList() throws Exception  {
    ArrayList<Preset> presets = new ArrayList<Preset>();
    presets.add(preset);
    presets.add(preset2);
    presetController.addPresets(presets);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(preset);
    expectedPresets.add(preset2);
    Assert.assertEquals(expectedPresets, presetController.getPresets());
  }

  @Test
  public void testRemovePreset() throws Exception {
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(preset);
    presetController.addPreset(preset);
    presetController.addPreset(preset2);
    presetController.removePreset(preset2);
    Assert.assertEquals(expectedPresets, presetController.getPresets());
  }

  @Test
  public void testGetPresetsByTag() throws Exception {
    HashSet<String> preset1Tags = new HashSet<>();
    preset1Tags.add("Piano");
    HashSet<String> preset2Tags = new HashSet<>();
    preset2Tags.add("Violin");
    when(preset.getTags()).thenReturn(preset1Tags);
    when(preset2.getTags()).thenReturn(preset2Tags);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();

    presetController.addPreset(preset);
    presetController.addPreset(preset2);
    expectedPresets.add(preset);

    Assert.assertEquals(expectedPresets, presetController.getPresetsByTag("Piano"));
  }

  @Test
  public void testGetPresetsByTagNoMatch() throws Exception  {
    HashSet<String> preset1Tags = new HashSet<>();
    preset1Tags.add("Piano");
    HashSet<String> preset2Tags = new HashSet<>();
    preset2Tags.add("Violin");
    when(preset.getTags()).thenReturn(preset1Tags);
    when(preset2.getTags()).thenReturn(preset2Tags);

    presetController.addPreset(preset);
    presetController.addPreset(preset2);

    Assert.assertEquals(new ArrayList<Preset>(), presetController.getPresetsByTag("Overview"));
  }

  @Test
  public void testGetPresetsByTagMultiple() throws Exception {
    HashSet<String> preset1Tags = new HashSet<>();
    preset1Tags.add("Piano");
    preset1Tags.add("Violin");
    HashSet<String> preset2Tags = new HashSet<>();
    preset2Tags.add("Violin");
    when(preset.getTags()).thenReturn(preset1Tags);
    when(preset2.getTags()).thenReturn(preset2Tags);
    Assert.assertEquals(presetController.getPresets(), presetController.getPresetsByTag("Piano"));
  }

  @Test
  public void testGetPresetsById() throws Exception {
    when(preset.getId()).thenReturn(1);
    when(preset2.getId()).thenReturn(2);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();


    presetController.addPreset(preset);
    presetController.addPreset(preset2);
    expectedPresets.add(preset);

    Assert.assertEquals(preset2, presetController.getPresetById(2));
  }

  @Test
  public void testGetPresetsByIdNoMatch() throws Exception {
      when(preset.getId()).thenReturn(1);
      when(preset2.getId()).thenReturn(2);
      ArrayList<Preset> expectedPresets = new ArrayList<Preset>();

      presetController.addPreset(preset);
      presetController.addPreset(preset2);

      Assert.assertEquals(null, presetController.getPresetById(3));
    }


  @Test
  public void testGetSetTags() {
  
    presetController.addTag("tag");
    presetController.addTag("tag1");
    HashSet<String> expectedSet = new HashSet<>();
    expectedSet.add("tag");
    expectedSet.add("tag1");
    Assert.assertEquals(expectedSet, new HashSet<String>(presetController.getTags()));

  }

  @Test
  public void testAddPresetNonExistentTags() throws SQLException {
    Preset preset = mock(Preset.class);
    when(preset.getName()).thenReturn("");
    HashSet<String> tags = new HashSet<>();
    tags.add("tag1");
    when(preset.getTags()).thenReturn((Set)tags);
    presetController.addPreset(preset);
    Assert.assertEquals(presetController.getTags(), tags);

  }

  @Test
  public void testAddAllTags() throws SQLException {
    HashSet<String> tags = new HashSet<>();
    tags.add("tag1");
    tags.add("tag2");
    presetController.addAllTags(tags);

    Assert.assertEquals(tags, presetController.getTags());
  }


  @Test
  public void testRemoveTag() throws SQLException {
    PresetController controller = new PresetController();
    HashSet<String> tags = new HashSet<>();
    tags.add("tag1");
    controller.addTag("tag2");
    controller.addAllTags(tags);
    controller.removeTag("tag2");
    Assert.assertEquals(tags, controller.getTags());
  }

  @Test
  public void testRemoveTagFromPreset() throws SQLException {
    PresetController controller = new PresetController();
    Preset preset1 = mock(Preset.class);
    when(preset1.getName()).thenReturn("");
    controller.addPreset(preset1);
    HashSet<String> tags = new HashSet<>();
    tags.add("tag1");
    tags.add("tag2");
    controller.addAllTags(tags);
    controller.removeTag("tag1");

    verify(preset1).removeTag("tag1");
  }
}