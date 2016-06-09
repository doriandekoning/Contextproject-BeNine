package com.benine.backend.preset.autopresetcreation;

import com.benine.backend.camera.*;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 *
 */
public abstract class AutoPresetCreatorTest {

  private IPCamera camera;
  private AutoPresetCreator apc;

  @Before
  public final void setup() throws CameraConnectionException, CameraBusyException {
    camera = mock(IPCamera.class);
    doReturn(new ZoomPosition(4, 4, 0)).when(camera).getPosition();
    doReturn(IPCamera.MAX_ZOOM).when(camera).getZoom();
    apc = spy(getCreator());
    doReturn(new ArrayList<ZoomPosition>()).when(apc).generatePositions(any(IPCamera.class), any(ArrayList.class));
  }

  @Test
  public abstract void testCreate();

  @Test
  public void testSetCamBusy() throws Exception {
    apc.createPresets(camera, new ArrayList<>(), name, description);
    Mockito.verify(camera).setBusy(true);
  }

  @Test
  public void testCamNotBusyAfterwards() throws Exception {
    AutoPresetCreator apc = getCreator();
    apc.createPresets(camera, new ArrayList<>(), name, description);
    Assert.assertFalse(camera.isBusy());
  }

  @Test (expected = CameraBusyException.class)
  public void testCamBusyExceptionThrown() throws Exception {
    doReturn(true).when(camera).isBusy();
    apc.createPresets(camera, new ArrayList<>(), name, description);
  }



  public abstract AutoPresetCreator getCreator();

}
