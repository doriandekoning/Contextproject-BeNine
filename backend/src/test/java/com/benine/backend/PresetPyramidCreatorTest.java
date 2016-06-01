package com.benine.backend;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
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
  }

  @Test
  public final void testCreate() {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 1, 1);
    ArrayList<Preset> expected = new ArrayList<>();
    //expected.add(new PresetFactory().createPreset(0, 180), )

  }

  @Test
  public final void testCreatePositionsOneOne()
          throws InterruptedException, CameraConnectionException, TimeoutException {
    PresetPyramidCreator ppc = new PresetPyramidCreator(1, 1, 1);
    Collection<Preset> presets =  (Collection)ppc.createPresets(cam);
    Collection<Preset> expectedPresets = new ArrayList<Preset>();
    expectedPresets.add(new PresetFactory().createPreset(cam, 2, 30));
    Assert.assertEquals(expectedPresets, presets);
  }

}
