package com.benine.backend.http;

import com.benine.backend.Logger;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Preset;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpExchange;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;

public class RecallPresetTest {

  private IPCamera ipcamera;
  private Preset preset;
  private RecallPresetHandler recallHandler;
  private Logger logger;
  private HttpExchange exchange;

  @Before
  public void setUp() throws CameraConnectionException{
    exchange = mock(HttpExchange.class);
    CameraController controller = mock(CameraController.class);
    ipcamera = mock(IPCamera.class);
    logger = mock(Logger.class);
    preset = new Preset(new Position(0,0), 100, 33,50,true,15,1,true);;
    recallHandler = new RecallPresetHandler(controller, logger);
    try {
      recallHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMovingCameraZoomPosition() throws Exception {
    try {
      recallHandler.handle(exchange);
    }catch (Exception e) {
      e.printStackTrace();
    }
    recallHandler.movingCamera(ipcamera, preset, exchange);
    verify(ipcamera).zoomTo(preset.getZoom());
  }

  @Test
  public void testMovingCameraFocusPosition() throws Exception {
    try {
      recallHandler.handle(exchange);
    }catch (Exception e) {
      e.printStackTrace();
    }
    recallHandler.movingCamera(ipcamera, preset, exchange);
    verify(ipcamera).moveFocus(preset.getFocus());
  }

  @Test
  public void testMovingCameraIrisPosition() throws Exception {
    try {
      recallHandler.handle(exchange);
    }catch (Exception e) {
      e.printStackTrace();
    }
    recallHandler.movingCamera(ipcamera, preset, exchange);
    verify(ipcamera).setIrisPos(preset.getIris());
  }

  @Test
  public void testMovingCameraAutofocus() throws Exception {
    try {
      recallHandler.handle(exchange);
    }catch (Exception e) {
      e.printStackTrace();
    }
    recallHandler.movingCamera(ipcamera, preset, exchange);
    verify(ipcamera).setAutoFocusOn(preset.isAutofocus());
  }

  @Test
  public void testMovingCameraZoomAutoiris() throws Exception {
    try {
      recallHandler.handle(exchange);
    }catch (Exception e) {
      e.printStackTrace();
    }
    recallHandler.movingCamera(ipcamera, preset, exchange);
    verify(ipcamera).setAutoIrisOn(preset.isAutoiris());
  }

  @Test
  public void testMovingCamera() throws Exception {
    try {
      recallHandler.handle(exchange);
    }catch (Exception e) {
      e.printStackTrace();
    }recallHandler.movingCamera(ipcamera, preset, exchange);
    
    verify(ipcamera).moveTo(any(Position.class), eq(preset.getPanspeed()), eq(preset.getTiltspeed()));
  }
}