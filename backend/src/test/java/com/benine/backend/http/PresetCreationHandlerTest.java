package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.OutputStream;

import org.junit.Test;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.DatabasePreset;
import com.sun.net.httpserver.HttpExchange;

import org.junit.Assert;
import org.junit.Before;

public class PresetCreationHandlerTest {
  private CameraController camera;
  private PresetCreationHandler handler;
  private HttpExchange exchangeMock;
  private OutputStream out;
  
  @Before
  public void initialize(){
    camera = mock(CameraController.class);
    handler = new PresetCreationHandler(camera);
    exchangeMock = mock(HttpExchange.class);
    out = mock(OutputStream.class);
    when(exchangeMock.getResponseBody()).thenReturn(out);
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
    Assert.assertEquals(preset.getFocus(), handler.createPreset(ipcamera).getFocus());
    Assert.assertEquals(preset.getIris(), handler.createPreset(ipcamera).getIris());
    Assert.assertEquals(preset.getPan(), handler.createPreset(ipcamera).getPan());
    Assert.assertEquals(preset.getTilt(), handler.createPreset(ipcamera).getTilt());
    Assert.assertEquals(preset.getZoom(), handler.createPreset(ipcamera).getZoom());
    Assert.assertEquals(preset.isAutofocus(), handler.createPreset(ipcamera).isAutofocus());
  }


  
  @Test
  public void testResponseMessageTrue() throws Exception {
    String response = "{\"succes\":\"true\"}"; 
    handler.responseMessage(exchangeMock, true);
    verify(exchangeMock).sendResponseHeaders(200, response.length());
  }
  
  @Test
  public void testResponseMessageFalse() throws Exception {
    String response = "{\"succes\":\"false\"}"; 
    handler.responseMessage(exchangeMock, false);
    verify(exchangeMock).sendResponseHeaders(200, response.length());
  }

}
