package com.benine.backend.http.presethandlers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.http.RequestHandlerTest;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;

public abstract class PresetRequestHandlerTest extends RequestHandlerTest {
  
  @Override
  public abstract PresetRequestHandler supplyHandler();
  
  @Test
  public void testGetPreset() {
    Preset preset = mock(IPCameraPreset.class);
    when(preset.getCameraId()).thenReturn(1);
    when(presetController.getPresetById(1)).thenReturn(preset);
    Assert.assertEquals(preset, ((PresetRequestHandler) getHandler()).getPreset(1));
  }
  
  @Test
  public void testGetPresetCamera() {
    IPCamera ipcamera = mock(IPCamera.class);
    when(cameraController.getCameraById(1)).thenReturn(ipcamera);
    Assert.assertEquals(ipcamera, ((PresetRequestHandler) getHandler()).getPresetCamera(1));
  }
  
  @Test
  public void testGetPresetnotpresetcamera() {
    Camera camera = mock(Camera.class);
    when(cameraController.getCameraById(2)).thenReturn(camera);
    Assert.assertEquals(null, ((PresetRequestHandler) getHandler()).getPresetCamera(2));
  }

}
