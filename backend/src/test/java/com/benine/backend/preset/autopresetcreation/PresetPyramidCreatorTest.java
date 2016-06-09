package com.benine.backend.preset.autopresetcreation;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.PresetTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class PresetPyramidCreatorTest extends AutoPresetCreatorTest {

  IPCamera cam = mock(IPCamera.class);
  PresetController presetController = mock(PresetController.class);

  @Before
  public void initialize() throws CameraConnectionException, SQLException {
    when(cam.getPosition()).thenReturn(new Position(0, 180));
    when(cam.getZoom()).thenReturn(0);
  }

  @Test
  public void testCreatePositionsSingleCenter()
          throws CameraConnectionException {
    ArrayList<SubView> subViews = new ArrayList<>();
    subViews.add(new SubView(new Coordinate(10, 90), new Coordinate(90, 10)));
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(1, 1, 1, 0, mock(PresetController.class)).generatePositions(cam, subViews);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition(), cam.getZoom()));

    Assert.assertEquals(expectedPositions, actualPositons);
  }


  @Test
  public void testCreatePositionsSingleOffCenter()
          throws CameraConnectionException {
    ArrayList<SubView> subViews = new ArrayList<>();
    subViews.add(new SubView(10, 90, 40, 60));
    subViews.add(new SubView(60, 40, 90, 10));
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(1, 1, 1, 0, presetController).generatePositions(cam, subViews);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan() - (IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt() + (IPCamera.VERTICAL_FOV_MAX/4), cam.getZoom()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan() + (IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt() - (IPCamera.VERTICAL_FOV_MAX/4), cam.getZoom()));
    Assert.assertEquals(expectedPositions, actualPositons);
  }


  @Test
  public void testCreateSubViews() {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 1, 1, 0, presetController);
    ArrayList<SubView> subViews = new ArrayList<>();
    subViews.add(new SubView(0, 100, 100, 0));
    Assert.assertEquals(subViews, ppc.generateSubViews());
  }

  @Test
  public void testCreateSubViews2x2x2() {
    PresetPyramidCreator ppc = new PresetPyramidCreator(2, 2, 2, 0, presetController);
    ArrayList<SubView> subViews = new ArrayList<>();
    subViews.add(new SubView(0, 100, 100, 0));
    subViews.add(new SubView(0, 100, 50, 50));
    subViews.add(new SubView(0, 50, 50, 0));
    subViews.add(new SubView(50, 100, 100, 50));
    subViews.add(new SubView(50, 50, 100, 0));
    Assert.assertEquals(new HashSet<>(subViews), new HashSet<>(ppc.generateSubViews()));
  }

  @Test
  public void testCreateSubViews1x1x2() {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 1, 2, 0, presetController);
    ArrayList<SubView> subViews = new ArrayList<>();
    subViews.add(new SubView(0, 100, 100, 0));
    subViews.add(new SubView(0, 100, 100, 0));
    Assert.assertEquals(subViews, ppc.generateSubViews());
  }

  @Test
  public void testCreateSubViews1x2x2() {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 2, 3, 0, presetController);
    ArrayList<SubView> subViews = new ArrayList<>();
    // First layer
    subViews.add(new SubView(0, 100, 100, 0));
    // Second layer
    subViews.add(new SubView(0, 100, 50, 0));
    subViews.add(new SubView(50, 100, 100, 0));
    // Third layer
    subViews.add(new SubView(0, 100, 25, 0));
    subViews.add(new SubView(25, 100, 50, 0));
    subViews.add(new SubView(50, 100, 75, 0));
    subViews.add(new SubView(75, 100, 100, 0));
    Collection<SubView> generated = ppc.generateSubViews();
    Assert.assertEquals(subViews.size(), generated.size());
    Assert.assertEquals(new HashSet<>(subViews), new HashSet<>(generated));
  }

  @Test(expected = AssertionError.class)
  public final void testAssertRows() {
    new PresetPyramidCreator(-1, 2, 3, 0.5, presetController);
  }

  @Test(expected = AssertionError.class)
  public final void testAssertColumns() {
    new PresetPyramidCreator(1, -2, 3, 0.5, presetController);
  }

  @Test(expected = AssertionError.class)
  public final void testAssertLevels() {
    new PresetPyramidCreator(1, 2, -5, 0.5, presetController);
  }

  @Override
  public void testCreate() {

  }

  @Override
  public AutoPresetCreator getCreator() {
    return new PresetPyramidCreator(2, 2, 2, 0, presetController);
  }

}