package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Preset;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

public class RecallPresetTest {

  private PresetCreationHandler handler;
  private CameraController controller;
  private IPCamera ipcamera;
  private Preset preset;
  private Position position;
  
  @Before
  public void setUp() {
    controller = mock(CameraController.class);
    ipcamera = mock(IPCamera.class);
    preset = new Preset(0,0,100,33,50,true, 15,1,true);
    position = new Position(preset.getPan(),preset.getTilt());
  }
  
  @Test
  public void testMovingCameraZoomPosition() throws CameraConnectionException{
    ipcamera.zoomTo(preset.getZoom());
    verify(ipcamera).zoomTo(preset.getZoom());    
  }
  
  @Test
  public void testMovingCameraFocusPosition() throws CameraConnectionException{
    ipcamera.moveFocus(preset.getFocus());
    verify(ipcamera).moveFocus(preset.getFocus());    
  }
  
  @Test
  public void testMovingCameraIrisPosition() throws CameraConnectionException{
    ipcamera.setIrisPos(preset.getIris());
    verify(ipcamera).setIrisPos(preset.getIris());    
  }
  
  @Test
  public void testMovingCameraAutofocus() throws CameraConnectionException{
    ipcamera.setAutoFocusOn(preset.isAutofocus());
    verify(ipcamera).setAutoFocusOn(preset.isAutofocus());    
  }
  
  @Test
  public void testMovingCameraZoomAutoiris() throws CameraConnectionException{
    ipcamera.setAutoIrisOn(preset.isAutoiris());
    verify(ipcamera).setAutoIrisOn(preset.isAutoiris());    
  }
  
  @Test
  public void testMovingCamera() throws CameraConnectionException{
    ipcamera.moveTo(position, preset.getPanspeed() , preset.getTiltspeed());
    verify(ipcamera).moveTo(position, preset.getPanspeed() , preset.getTiltspeed());
  }
}
