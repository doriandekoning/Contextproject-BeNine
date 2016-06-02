package com.benine.backend;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
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
                                           cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX/2),
                                           cam.getZoomPosition()));
    expectedPositions.add(new ZoomPosition(0.0,
                                           cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX/2),
                                           cam.getZoomPosition()));
    Assert.assertEquals(expectedPositions, actualPositons);
  }

  @Test
  public void testCreatePositions1x2x1()
          throws CameraConnectionException {
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(1, 2, 1, 0).generatePositions(cam);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX/2),
            180.0,
            cam.getZoomPosition()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX/2),
            180.0,
            cam.getZoomPosition()));
    Assert.assertEquals(expectedPositions, actualPositons);
  }

  @Override
  public void testCreate() {

  }

}
