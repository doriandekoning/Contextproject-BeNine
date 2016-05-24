package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.benine.backend.camera.CameraConnectionException;

/**
 * Test class to test the IP Camera Iris functions class.
 * The mock server is used to simulate the camera.
 */
public class IpcameraIrisTest {

  private IPCamera camera;
  
  @Before
  public final void setUp(){
    camera = Mockito.spy(new IPCamera("test"));
  }
  
  public void setCameraBehaviour(String cmd, String response) throws IpcameraConnectionException {
    Mockito.doReturn(response).when(camera).sendCommand("aw_ptz?cmd=%23" + cmd + "&res=1");
  }
  
  @Test
  public final void testSetAutoIrisOff() throws CameraConnectionException {
    setCameraBehaviour("D30", "d30");

    camera.setAutoIrisOn(false);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23D30&res=1");
  }
  
  @Test
  public final void testSetAutoIrisOn() throws CameraConnectionException {
    setCameraBehaviour("D31", "d31");

    camera.setAutoIrisOn(true);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23D31&res=1");
  }
  
  @Test
  public final void testIsAutoIrisOff() throws CameraConnectionException {
    setCameraBehaviour("D3", "d30");
    boolean res = camera.isAutoIrisOn();
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23D3&res=1");
    assertFalse(res);
  }
  
  @Test
  public final void testIsAutoIrisOn() throws CameraConnectionException {
    setCameraBehaviour("D3", "d31");

    boolean res = camera.isAutoIrisOn();
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23D3&res=1");
    assertTrue(res);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testIsAutoIrisOnException() throws CameraConnectionException {
    setCameraBehaviour("D3", "p31");
    camera.isAutoIrisOn();
  }
  
  @Test
  public final void testSetIrisPosition() throws CameraConnectionException {
    setCameraBehaviour("I80", "iC80");

    camera.setIrisPosition(80);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23I80&res=1");
  }
  
  @Test
  public final void testSetIrisPosition2() throws CameraConnectionException {
    setCameraBehaviour("I02", "iC02");

    camera.setIrisPosition(2);
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23I02&res=1");
  }
  
  @Test
  public final void testGetIrisPosition() throws CameraConnectionException {
    setCameraBehaviour("GI", "giD421");
    int res = camera.getIrisPosition();
    
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23GI&res=1");
    
    assertEquals(res, 3394, 0.000001);
  }

  @Test
  public final void testMoveIris() throws CameraConnectionException {
    setCameraBehaviour("I40", "iC40");

    camera.moveIris(40);

    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23I40&res=1");
  }
}
