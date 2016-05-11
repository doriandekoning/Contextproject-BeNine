package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.DatabasePreset;

import org.junit.Assert;

public class PresetCreationHandlerTest {

  @Test
  public void testGetCameraPositions() throws CameraConnectionException {
    CameraController controller = mock(CameraController.class);
    PresetCreationHandler handler = new PresetCreationHandler(controller);
    IPCamera ipcamera = mock(IPCamera.class);
    when(ipcamera.getFocusPos()).thenReturn(33);
    when(ipcamera.getIrisPos()).thenReturn(50);
    when(ipcamera.getPosition()).thenReturn(new Position(0, 0));
    when(ipcamera.getZoomPosition()).thenReturn(100);  
    when(ipcamera.isAutoFocusOn()).thenReturn(true);

    DatabasePreset preset = new DatabasePreset(0,0,100,33,50,true);
    Assert.assertEquals(preset.getFocus(), handler.createPreset(ipcamera).getFocus());
    Assert.assertEquals(preset.getIris(), handler.createPreset(ipcamera).getIris());
    Assert.assertEquals(preset.getPan(), handler.createPreset(ipcamera).getPan());
    Assert.assertEquals(preset.getTilt(), handler.createPreset(ipcamera).getTilt());
    Assert.assertEquals(preset.getZoom(), handler.createPreset(ipcamera).getZoom());
    Assert.assertEquals(preset.isAutofocus(), handler.createPreset(ipcamera).isAutofocus());
  }


}
