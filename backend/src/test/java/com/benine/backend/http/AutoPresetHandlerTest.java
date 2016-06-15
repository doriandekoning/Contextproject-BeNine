package com.benine.backend.http;

import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test the auto preset handler.
 */
public abstract class AutoPresetHandlerTest extends RequestHandlerTest {

  @Before
  public void init() {
    setPath("/presets/autoCreateSubViews");
  }

  @Test
  public void testGetCreatorNoArgs() {
    MultiMap<String> parameters = new MultiMap<>();
    setParameters(parameters);
    PresetPyramidCreator expectedCreator = new PresetPyramidCreator(3, 3, 3, 0, Mockito.mock(PresetController.class));
    PresetPyramidCreator actualCreator = supplyHandler().getPyramidPresetCreator(requestMock);
    Assert.assertEquals(actualCreator, expectedCreator);
  }


  @Test (expected = IllegalArgumentException.class)
  public void testGetCreatorToManyColumns() {
    MultiMap<String> params = new MultiMap<>();
    params.add("columns", "8");
    setParameters(params);
    supplyHandler().getPyramidPresetCreator(requestMock);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCreatorToManyRows() {
    MultiMap<String> params = new MultiMap<>();
    params.add("rows", "8");
    setParameters(params);
    supplyHandler().getPyramidPresetCreator(requestMock);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCreatorToManyLevels() {
    MultiMap<String> params = new MultiMap<>();
    params.add("levels", "8");
    setParameters(params);
    supplyHandler().getPyramidPresetCreator(requestMock);
  }
  @Test (expected = IllegalArgumentException.class)
  public void testGetCreatorToLittleColumns() {
    MultiMap<String> params = new MultiMap<>();
    params.add("columns", "0");
    setParameters(params);
    supplyHandler().getPyramidPresetCreator(requestMock);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCreatorToLittleRows() {
    MultiMap<String> params = new MultiMap<>();
    params.add("rows", "0");
    setParameters(params);
    supplyHandler().getPyramidPresetCreator(requestMock);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCreatorToLittleLevels() {
    MultiMap<String> params = new MultiMap<>();
    params.add("levels", "0");
    setParameters(params);
    supplyHandler().getPyramidPresetCreator(requestMock);
  }



  public abstract AutoPresetHandler supplyHandler();

}
