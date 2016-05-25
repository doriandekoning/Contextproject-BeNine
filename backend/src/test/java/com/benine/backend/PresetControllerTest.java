package com.benine.backend;

import com.benine.backend.database.MySQLDatabase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dorian on 18-5-16.
 */
public class PresetControllerTest {
  
  @Test
  public void testAddPreset() throws Exception {
    PresetController controller = new PresetController();
    Preset preset = mock(Preset.class);
    controller.addPreset(preset);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(preset);
    Assert.assertEquals(expectedPresets, controller.getPresets());
  }

  @Test
  public void testAddPresetList() throws Exception  {
    PresetController controller = new PresetController();
    ArrayList<Preset> presets = new ArrayList<Preset>();
    Preset preset1 = mock(Preset.class);
    Preset preset2 = mock(Preset.class);
    presets.add(preset1);
    presets.add(preset2);
    controller.addPresets(presets);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(preset1);
    expectedPresets.add(preset2);
    Assert.assertEquals(expectedPresets, controller.getPresets());
  }

  @Test
  public void testRemovePreset() throws Exception {
    PresetController controller = new PresetController();
    Preset preset1 = mock(Preset.class);
    Preset preset2 = mock(Preset.class);
    controller.addPreset(preset1);
    controller.addPreset(preset2);

    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(preset1);
    // The actual method call to test.
    controller.removePreset(preset2);
    Assert.assertEquals(expectedPresets, controller.getPresets());
  }

  @Test
  public void testGetPresetsByTag() throws Exception {
    PresetController controller = new PresetController();
    Preset preset1 = mock(Preset.class);
    Preset preset2 = mock(Preset.class);
    HashSet<String> preset1Tags = new HashSet<>();
    preset1Tags.add("Piano");
    HashSet<String> preset2Tags = new HashSet<>();
    preset2Tags.add("Violin");
    when(preset1.getTags()).thenReturn(preset1Tags);
    when(preset2.getTags()).thenReturn(preset2Tags);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();


    controller.addPreset(preset1);
    controller.addPreset(preset2);
    expectedPresets.add(preset1);

    Assert.assertEquals(expectedPresets, controller.getPresetsByTag("Piano"));
  }

  @Test
  public void testGetPresetsByTagNoMatch() throws Exception  {
    PresetController controller = new PresetController();
    Preset preset1 = mock(Preset.class);
    Preset preset2 = mock(Preset.class);
    HashSet<String> preset1Tags = new HashSet<>();
    preset1Tags.add("Piano");
    HashSet<String> preset2Tags = new HashSet<>();
    preset2Tags.add("Violin");
    when(preset1.getTags()).thenReturn(preset1Tags);
    when(preset2.getTags()).thenReturn(preset2Tags);

    controller.addPreset(preset1);
    controller.addPreset(preset2);

    Assert.assertEquals(new ArrayList<Preset>(), controller.getPresetsByTag("Overview"));
  }

  @Test
  public void testGetPresetsByTagMultiple() throws Exception {
    PresetController controller = new PresetController();
    Preset preset1 = mock(Preset.class);
    Preset preset2 = mock(Preset.class);
    HashSet<String> preset1Tags = new HashSet<>();
    preset1Tags.add("Piano");
    preset1Tags.add("Violin");
    HashSet<String> preset2Tags = new HashSet<>();
    preset2Tags.add("Violin");
    when(preset1.getTags()).thenReturn(preset1Tags);
    when(preset2.getTags()).thenReturn(preset2Tags);
    Assert.assertEquals(controller.getPresets(), controller.getPresetsByTag("Piano"));
  }

  @Test
  public void testGetPresetsById() throws Exception {
    PresetController controller = new PresetController();
    Preset preset1 = mock(Preset.class);
    Preset preset2 = mock(Preset.class);

    when(preset1.getId()).thenReturn(1);
    when(preset2.getId()).thenReturn(2);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();


    controller.addPreset(preset1);
    controller.addPreset(preset2);
    expectedPresets.add(preset1);

    Assert.assertEquals(preset2, controller.getPresetById(2));
  }

  @Test
  public void testGetPresetsByIdNoMatch() throws Exception {
      PresetController controller = new PresetController();
      Preset preset1 = mock(Preset.class);
      Preset preset2 = mock(Preset.class);

      when(preset1.getId()).thenReturn(1);
      when(preset2.getId()).thenReturn(2);
      ArrayList<Preset> expectedPresets = new ArrayList<Preset>();


      controller.addPreset(preset1);
      controller.addPreset(preset2);

      Assert.assertEquals(null, controller.getPresetById(3));
    }


  @Test
  public void testGetSetTags() {
    PresetController controller = new PresetController();
    controller.addTag("tag");
    controller.addTag("tag1");
    HashSet<String> expectedSet = new HashSet<>();
    expectedSet.add("tag");
    expectedSet.add("tag1");
    Assert.assertEquals(expectedSet, new HashSet<String>(controller.getTags()));

  }

}
