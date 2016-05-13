package com.benine.backend.http;

import com.benine.backend.Logger;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Preset;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;

public class RecallPresetTest {

  private IPCamera ipcamera;
  private Preset preset;
  private RecallPresetHandler recallHandler;
  private Logger logger;

  @Before
  public void setUp() {
    CameraController controller = mock(CameraController.class);
    ipcamera = mock(IPCamera.class);
    logger = mock(Logger.class);
    preset = new Preset(new Position(0,0), 100, 33,50,true,15,1,true);;
    recallHandler = new RecallPresetHandler(controller, logger);
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