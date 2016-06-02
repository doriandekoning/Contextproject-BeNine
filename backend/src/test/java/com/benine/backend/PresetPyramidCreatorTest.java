package com.benine.backend;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 *
 */
public class PresetPyramidCreatorTest extends AutoPresetCreatorTest {

  IPCamera cam = mock(IPCamera.class);


  @Before
  public final void setup() throws CameraConnectionException, TimeoutException, InterruptedException {
   // doReturn().when(cam).waitUntilAtPosition(any(Position.class), anyInt(), anyLong());
    doReturn(new Position(30, 20)).when(cam).getPosition();
    doReturn(0).when(cam).getZoomPosition();
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) {
        return null;
      }
    }).when(cam).moveTo(any(Position.class), anyInt(), anyInt());
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) {
        return null;
      }
    }).when(cam).waitUntilAtPosition(any(Position.class), anyInt(), anyInt());
  }

  @Test
  public final void testCreate() {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 1, 1, 0);
    ArrayList<Preset> expected = new ArrayList<>();
    //expected.add(new PresetFactory().createPreset(0, 180), )

  }

  @Test
  public final void testCreatePositionsOneOne()
          throws InterruptedException, CameraConnectionException, TimeoutException {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 1, 1, 0);
    ppc.createPresets(cam);
    verify(cam).moveTo(eq(new Position(0, 180.0)), anyInt(), anyInt());
  }

  @Test
  public final void testCreatePositionsTwoByOne()
          throws InterruptedException, CameraConnectionException, TimeoutException {
    PresetPyramidCreator ppc = new PresetPyramidCreator(2, 1, 1, 0);
    ppc.createPresets(cam);


    verify(cam).moveTo(eq(new Position(0, 180-(IPCamera.HORIZONTAL_FOV_MAX/2))), anyInt(), anyInt());
    verify(cam).moveTo(eq(new Position(0, 180+(IPCamera.HORIZONTAL_FOV_MAX/2))), anyInt(), anyInt());
  }


  @Test
  public final void testCreatePositionsOneByTwo()
          throws InterruptedException, CameraConnectionException, TimeoutException {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 2, 1, 0);
    ppc.createPresets(cam);

    verify(cam).moveTo(eq(new Position(-60.3, 180.0)), anyInt(), anyInt());
    verify(cam).moveTo(eq(new Position(60.3, 180.0)), anyInt(), anyInt());
  }

}
