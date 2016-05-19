package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.benine.backend.Logger;
import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpExchange;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;


public class PresetCreationHandlerTest {

  private ServerController serverController;
  private CameraController camController;
  private PresetCreationHandler handler;
  OutputStream out = mock(OutputStream.class);
  private IPCamera ipcamera = mock(IPCamera.class);
  private Preset preset;
  private HttpExchange exchange = mock(HttpExchange.class);
  
  @Before
  public void setUp() throws CameraConnectionException {
    ServerController.setConfigPath("resources" + File.separator 
                          + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    ipcamera = mock(IPCamera.class);
    when(ipcamera.getFocusPosition()).thenReturn(33);
    when(ipcamera.getIrisPosition()).thenReturn(50);

    camController = mock(CameraController.class);
    when(camController.getCameraById(1)).thenReturn(ipcamera);
    serverController.setCameraController(camController);
    when(exchange.getResponseBody()).thenReturn(out);
    handler = new PresetCreationHandler();

    when(ipcamera.getPosition()).thenReturn(new Position(0, 0));
    when(ipcamera.getZoomPosition()).thenReturn(100);  
    when(ipcamera.isAutoFocusOn()).thenReturn(true);
    when(ipcamera.isAutoIrisOn()).thenReturn(true);
    when(ipcamera.getId()).thenReturn(1);
    Preset[] presets = {};
    when(ipcamera.getPresets()).thenReturn(presets);
    
    preset = new Preset(new Position(0,0), 100, 33,50,true,15,1,true);
    
  }
  
  @Test
  public void testCreatePreset() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+ipcamera.getId()+"/zoom?autoIrisOn=true");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      handler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(camController).addPreset(1, preset);
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
  public void testGetPos() {
    Assert.assertEquals(preset.getPosition(), handler.createPreset(ipcamera).getPosition());
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
