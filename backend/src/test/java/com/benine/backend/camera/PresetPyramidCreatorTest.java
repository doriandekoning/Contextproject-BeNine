package com.benine.backend.camera;//TODO add Javadoc comment

import com.benine.backend.AutoPresetCreatorTest;
import com.benine.backend.Preset;
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
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 2, 3);
    ArrayList<Preset> expected = new ArrayList<>();
    expected.add(null);
    expected.add(null);
    expected.add(null);
    expected.add(null);
    expected.add(null);
    expected.add(null);
    Assert.assertEquals(expected, ppc.createPresets(null));
  }
}
