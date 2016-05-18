package com.benine.backend;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dorian on 18-5-16.
 */
public class PresetControllerTest {

  @Test
  public void testAddPreset() {
    PresetController controller = new PresetController();
    Preset preset = mock(Preset.class);
    controller.addPreset(preset);
    ArrayList<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(preset);
    Assert.assertEquals(expectedPresets, controller.getPresets());
  }

  @Test
  public void testAddPresetList() {
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
  public void testRemovePreset() {
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
  public void testGetPresetsByTag() {
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
  public void testGetPresetsByTagNoMatch() {
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
  public void testGetPresetsByTagMultiple() {
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

}
