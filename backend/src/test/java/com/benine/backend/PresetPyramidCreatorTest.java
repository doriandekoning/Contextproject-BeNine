package com.benine.backend;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import com.benine.backend.camera.ipcameracontrol.ZoomPosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

/**
 *
 */
public class PresetPyramidCreatorTest extends AutoPresetCreatorTest {

  IPCamera cam = mock(IPCamera.class);

  @Before
  public void setup() throws CameraConnectionException {
    when(cam.getPosition()).thenReturn(new Position(0, 180));
    when(cam.getZoomPosition()).thenReturn(0);
  }

  @Test
  public void testCreatePositions1x1x1()
          throws CameraConnectionException {
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(1, 1, 1, 0).generatePositions(cam);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition(), cam.getZoomPosition()));

    Assert.assertEquals(expectedPositions, actualPositons);
  }

  @Test
  public void testCreatePositions2x1x1()
          throws CameraConnectionException {
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(2, 1, 1, 0).generatePositions(cam);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(0.0,
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX/4),
                                           cam.getZoomPosition()));
    expectedPositions.add(new ZoomPosition(0.0,
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX/4),
                                           cam.getZoomPosition()));
    Assert.assertEquals(expectedPositions, actualPositons);
  }

  @Test
  public void testCreatePositions1x2x1()
          throws CameraConnectionException {
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(1, 2, 1, 0).generatePositions(cam);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX/4),
            180.0,
            cam.getZoomPosition()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX/4),
            180.0,
            cam.getZoomPosition()));
    Assert.assertEquals(expectedPositions, actualPositons);
  }

  @Test
  public void testCreatePositions2x2x1()
          throws CameraConnectionException {
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(2, 2, 1, 0).generatePositions(cam);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoomPosition()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoomPosition()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoomPosition()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoomPosition()));
    Assert.assertTrue(expectedPositions.containsAll(actualPositons));
    Assert.assertTrue(actualPositons.containsAll(expectedPositions));
  }

  @Test
  public void testCreatePositions3x3x1()
          throws CameraConnectionException {
    ArrayList<ZoomPosition> actualPositons =  new ArrayList<>(new PresetPyramidCreator(3, 3, 1, 0).generatePositions(cam));
    ArrayList<ZoomPosition> expectedPositions = new ArrayList<>();

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoomPosition()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoomPosition()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoomPosition()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoomPosition()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt(),
            cam.getZoomPosition()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt(),
            cam.getZoomPosition()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan(),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoomPosition()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan(),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoomPosition()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan(),
            cam.getPosition().getTilt(),
            cam.getZoomPosition()));

    Assert.assertTrue(expectedPositions.containsAll(actualPositons));
    Assert.assertTrue(actualPositons.containsAll(expectedPositions));
  }

  @Test(expected = AssertionError.class)
  public final void testAssertRows() {
    new PresetPyramidCreator(-1, 2, 3, 0.5);
  }

  @Test(expected = AssertionError.class)
  public final void testAssertColumns() {
    new PresetPyramidCreator(1, -2, 3, 0.5);
  }

  @Test(expected = AssertionError.class)
  public final void testAssertLevels() {
    new PresetPyramidCreator(1, 2, -5, 0.5);
  }

  @Override
  public void testCreate() {

  }

}
