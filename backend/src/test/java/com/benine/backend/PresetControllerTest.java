package com.benine.backend;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

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
}
