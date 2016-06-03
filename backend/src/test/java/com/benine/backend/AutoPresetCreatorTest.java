package com.benine.backend;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 *
 */
public abstract class AutoPresetCreatorTest {

  @Test
  public abstract void testCreate();

  @Test
  public void testSetCamBusy() throws Exception {
    IPCamera cam = spy(new IPCamera("test"));
    AutoPresetCreator apc = getCreator();
    apc.createPresets(cam);
    Mockito.verify(cam).setBusy(true);
  }

  @Test
  public void testCamNotBusyAfterwards() throws Exception {
    IPCamera cam = spy(new IPCamera("test"));
    AutoPresetCreator apc = getCreator();
    apc.createPresets(cam);
    Assert.assertFalse(cam.isBusy());
  }

  @Test (expected = CameraBusyException.class)
  public void testCamBusyExceptionThrown() throws Exception {
    IPCamera cam = spy(new IPCamera("test"));
    AutoPresetCreator apc = getCreator();
    apc.createPresets(cam);
  }



  public abstract AutoPresetCreator getCreator();

}
