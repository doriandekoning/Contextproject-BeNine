package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.Config;
import com.benine.backend.Logger;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class to test the IP Camera Focus function class.
 * The mock server is used to simulate the camera.
 */
public class IpcameraFocusTest {

  private IPCamera camera;
  private CameraController cameraController = mock(CameraController.class);
  private Config config = mock(Config.class);
  private Logger logger = mock(Logger.class);
  
  @Before
  public final void setUp() {
    when(config.getValue("IPCameraTimeOut")).thenReturn("2");
    when(cameraController.getConfig()).thenReturn(config);
    when(cameraController.getLogger()).thenReturn(logger);
    camera = Mockito.spy(new IPCamera("test", cameraController));
  }
  
  public void setCameraBehaviour(String cmd, String response) throws IpcameraConnectionException {
    Mockito.doReturn(response).when(camera).sendCommand("aw_ptz?cmd=%23" + cmd + "&res=1");
  }
  
  @Test
  public final void testGetFocusPosition() throws CameraConnectionException {
    setCameraBehaviour("GF", "gfA42");
    int res = camera.getFocusPosition();    
    assertEquals(res, 1261, 0.000001);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetFocusPositionException() throws CameraConnectionException {
    setCameraBehaviour("GF", "ggA42");
    camera.getFocusPosition();
  }
  
  @Test
  public final void testSetFocus() throws CameraConnectionException {
    setCameraBehaviour("AXFFFF", "axfFFF");
    camera.setFocusPosition(2882);
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23AXFFFF&res=1");
  }
  
  @Test
  public final void testMoveFocus() throws CameraConnectionException {
    setCameraBehaviour("F80", "fS80");
    camera.moveFocus(80);   
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23F80&res=1");
  }
  
  @Test
  public final void testMoveFocus2() throws CameraConnectionException {
    setCameraBehaviour("F02", "fS02");
    camera.moveFocus(2);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23F02&res=1");
  }
  
  @Test
  public final void testIsAutoFocusOn() throws CameraConnectionException {
    setCameraBehaviour("D1", "d11");
    boolean res = camera.isAutoFocusOn();
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23D1&res=1");
    assertTrue(res);
  }
  
  @Test
  public final void testIsAutoFocusOff() throws CameraConnectionException {
    setCameraBehaviour("D1", "d10");
    boolean res = camera.isAutoFocusOn();
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23D1&res=1");
    assertFalse(res);
  }
  
  @Test(expected = CameraConnectionException.class)
  public final void testIsAutoFocusOffException() throws CameraConnectionException {
    setCameraBehaviour("D1", "K10");
    camera.isAutoFocusOn();
  }
  
  @Test
  public final void testSetAutoFocusOff() throws CameraConnectionException {
    setCameraBehaviour("D10", "d10");
    camera.setAutoFocusOn(false);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23D10&res=1");
  }
  
  @Test
  public final void testSetAutoFocusOn() throws CameraConnectionException {
    setCameraBehaviour("D11", "d11");
    camera.setAutoFocusOn(true);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23D11&res=1");
  }
}
