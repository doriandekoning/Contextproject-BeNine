package com.benine.backend.camera;//TODO add Javadoc comment

import com.benine.backend.AutoPresetCreatorTest;
import com.benine.backend.Preset;
import com.benine.backend.PresetFactory;
import com.benine.backend.PresetPyramidCreator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 *
 */
public class PresetPyramidCreatorTest extends AutoPresetCreatorTest {

  @Test
  public final void testCreate() {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 1, 1);
    ArrayList<Preset> expected = new ArrayList<>();
    //expected.add(new PresetFactory().createPreset(0, 180), )

  }
}
