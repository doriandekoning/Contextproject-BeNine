package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.DatabasePreset;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class PresetCreationHandlerTest {

  @Test
  public void testResponseMessageTrue() {
    CameraController camera = mock(CameraController.class);
    PresetCreationHandler handler = new PresetCreationHandler(camera);
    String response = "{\"succes\":\"true\"}"; 
    String actual= handler.responseMessage(true);
    Assert.assertTrue(actual.equals(response));
  }
  
  @Test
  public void testResponseMessageFalse() {
    CameraController camera = mock(CameraController.class);
    PresetCreationHandler handler = new PresetCreationHandler(camera);
    String response = "{\"succes\":\"false\"}"; 
    String actual= handler.responseMessage(false);
    Assert.assertTrue(actual.equals(response));
  }
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
    Assert.assertEquals(preset.getFocus(), handler.getCameraPositions(ipcamera).getFocus());
    Assert.assertEquals(preset.getIris(), handler.getCameraPositions(ipcamera).getIris());
    Assert.assertEquals(preset.getPan(), handler.getCameraPositions(ipcamera).getPan());
    Assert.assertEquals(preset.getTilt(), handler.getCameraPositions(ipcamera).getTilt());
    Assert.assertEquals(preset.getZoom(), handler.getCameraPositions(ipcamera).getZoom());
    Assert.assertEquals(preset.isAutofocus(), handler.getCameraPositions(ipcamera).isAutofocus());
    
    
    
  }

//  @Test
//  public void testHandle() {
//    CameraController controller = mock(CameraController.class);
//    PresetCreationHandler handler = new PresetCreationHandler(controller);
//    
//    
//  }


}
