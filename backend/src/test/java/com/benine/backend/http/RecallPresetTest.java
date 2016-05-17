package com.benine.backend.http;

import com.benine.backend.Logger;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.Database;
import com.sun.net.httpserver.HttpExchange;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;

public class RecallPresetTest {

  private IPCamera ipcamera;
  private Preset preset;
  private RecallPresetHandler recallHandler;
  private Logger logger;
  private ServerController serverController;
  private CameraController camController = mock(CameraController.class);
  private HttpExchange exchange = mock(HttpExchange.class);
  private OutputStream out = mock(OutputStream.class);

  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "serverControllertest.conf");
    serverController = ServerController.getInstance();
    
    ipcamera = mock(IPCamera.class);
    when(camController.getCameraById(1)).thenReturn(ipcamera);
    serverController.setCameraController(camController);
    when(exchange.getResponseBody()).thenReturn(out);
    logger = mock(Logger.class);
    preset = new Preset(new Position(0,0), 100, 33,50,true,15,1,true);;
    recallHandler = new RecallPresetHandler(logger);
  }
  
  @Test
  public void testRecallingPreset() throws Exception {   
    URI uri = new  URI("http://localhost/camera/1/recallPreset?presetid=1");
    Database database = mock(Database.class);
    when(database.getPreset(1, 1)).thenReturn(new Preset(new Position(1, 2), 1, 0, 0, false, 0, 0, false));
    when(exchange.getRequestURI()).thenReturn(uri);

    serverController.setDatabase(database);
    doThrow(new SQLException("test exception")).when(database).getAllPresetsCamera(1);
    recallHandler.handle(exchange);

    verify(ipcamera).moveTo(new Position(1, 2), 0, 0);
  }
  
  @Test
  public void testMalformedURI() throws Exception {
    URI uri = new  URI("http://localhost/camera/1/focus?position=4&position=3&autoFocusOn=false");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      recallHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }

  @Test
  public void testMovingCameraZoomPosition() throws CameraConnectionException {
    recallHandler.movingCamera(ipcamera, preset);
    verify(ipcamera).zoomTo(preset.getZoom());
  }

  @Test
  public void testMovingCameraFocusPosition() throws CameraConnectionException {
    recallHandler.movingCamera(ipcamera, preset);
    verify(ipcamera).moveFocus(preset.getFocus());
  }

  @Test
  public void testMovingCameraIrisPosition() throws CameraConnectionException {
    recallHandler.movingCamera(ipcamera, preset);
    verify(ipcamera).setIrisPos(preset.getIris());
  }

  @Test
  public void testMovingCameraAutofocus() throws CameraConnectionException {
    recallHandler.movingCamera(ipcamera, preset);
    verify(ipcamera).setAutoFocusOn(preset.isAutofocus());
  }

  @Test
  public void testMovingCameraZoomAutoiris() throws CameraConnectionException {
    recallHandler.movingCamera(ipcamera, preset);
    verify(ipcamera).setAutoIrisOn(preset.isAutoiris());
  }

  @Test
  public void testMovingCamera() throws CameraConnectionException {
    recallHandler.movingCamera(ipcamera, preset);
    verify(ipcamera).moveTo(any(Position.class), eq(preset.getPanspeed()), eq(preset.getTiltspeed()));
  }
}