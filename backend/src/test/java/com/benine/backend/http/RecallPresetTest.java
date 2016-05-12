package com.benine.backend.http;

import com.benine.backend.Main;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Preset;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class RecallPresetTest {

  private CameraController controller;
  private IPCamera ipcamera;
  private Preset preset;
  private Position position;
  private RecallPreset recallHandler;
  private HttpExchange exchange;
  private OutputStream out;

  @Before
  public void setUp() {
    controller = mock(CameraController.class);
    ipcamera = mock(IPCamera.class);
    preset = new Preset(0, 0, 100, 33, 50, true, 15, 1, true);
    position = new Position(preset.getPan(), preset.getTilt());
    recallHandler = new RecallPreset(controller);
    exchange = mock(HttpExchange.class);
    out = mock(OutputStream.class);
    when(exchange.getResponseBody()).thenReturn(out);
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