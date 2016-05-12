package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Preset;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

public class PresetCreationHandlerTest {

  private CameraController controller;
  private PresetCreationHandler handler;
  private IPCamera ipcamera;
  private Preset preset;
  
  @Before
  public void setUp() throws CameraConnectionException{
    controller = mock(CameraController.class);
    handler = new PresetCreationHandler(controller);
    ipcamera = mock(IPCamera.class);
    when(ipcamera.getFocusPos()).thenReturn(33);
    when(ipcamera.getIrisPos()).thenReturn(50);
    when(ipcamera.getPosition()).thenReturn(new Position(0, 0));
    when(ipcamera.getZoomPosition()).thenReturn(100);  
    when(ipcamera.isAutoFocusOn()).thenReturn(true);
    when(ipcamera.isAutoIrisOn()).thenReturn(true);
    //Need two more rules with a when..... panspeed and tiltspeed then...
    preset = new Preset(0,0,100,33,50,true, 15,1,true);
    
  }
  @Test
  public void testGetFocusPos() {
    Assert.assertEquals(preset.getFocus(), handler.createPreset(ipcamera).getFocus());
  }
  @Test
  public void testGetIrisPos() {
    Assert.assertEquals(preset.getIris(), handler.createPreset(ipcamera).getIris());
  } 
  
  @Test
  public void testGetPanPos() {
    Assert.assertEquals(preset.getPan(), handler.createPreset(ipcamera).getPan());
  } 
  
  @Test
  public void testGetTiltPos() {
    Assert.assertEquals(preset.getTilt(), handler.createPreset(ipcamera).getTilt());
  } 
  
  @Test
  public void testGetZoomPos() {
    Assert.assertEquals(preset.getZoom(), handler.createPreset(ipcamera).getZoom());
  } 
  
  @Test
  public void testAutofocusOn() {
    Assert.assertEquals(preset.isAutofocus(), handler.createPreset(ipcamera).isAutofocus());    
  }

  @Test
  public void testAutoIrisOn() {
    Assert.assertEquals(preset.isAutoiris(), handler.createPreset(ipcamera).isAutoiris());    
  }
}
