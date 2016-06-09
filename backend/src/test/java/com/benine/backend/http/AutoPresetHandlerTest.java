package com.benine.backend.http;//TODO add Javadoc comment

import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 */
public abstract class AutoPresetHandlerTest extends RequestHandlerTest {

  @Test
  public void testGetCreatorNoArgs() {
    setPath("/presets/autoCreateSubViews");
    MultiMap<String> parameters = new MultiMap<>();
    setParameters(parameters);
    PresetPyramidCreator expectedCreator = new PresetPyramidCreator(3, 3, 3, 0, Mockito.mock(PresetController.class));
    PresetPyramidCreator actualCreator = supplyHandler().getPyramidPresetCreator(requestMock);
    Assert.assertEquals(actualCreator, expectedCreator);
  }

  public abstract AutoPresetHandler supplyHandler();

}
