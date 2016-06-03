package com.benine.backend;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomPosition;
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
  public final void setup() throws CameraConnectionException {
    camera = spy(new IPCamera("test"));
    doReturn(new ZoomPosition(4, 4, 0)).when(camera).getPosition();
    doReturn(IPCamera.MAX_ZOOM).when(camera).getZoomPosition();
    apc = spy(getCreator());
  }

  @Test
  public abstract void testCreate();

  @Test
  public void testSetCamBusy() throws Exception {
    doReturn(new ArrayList<ZoomPosition>()).when(apc).generatePositions(any(IPCamera.class));
    apc.createPresets(camera);
    Mockito.verify(camera).setBusy(true);
  }

  @Test
  public void testCamNotBusyAfterwards() throws Exception {
    AutoPresetCreator apc = getCreator();
    apc.createPresets(camera);
    Assert.assertFalse(camera.isBusy());
  }

  @Test (expected = CameraBusyException.class)
  public void testCamBusyExceptionThrown() throws Exception {
    AutoPresetCreator apc = getCreator();
    apc.createPresets(camera);
  }



  public abstract AutoPresetCreator getCreator();

}
