package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.Config;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.*;
import com.benine.backend.preset.IPCameraPreset;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

/**
 * Test class to test the IP Camera class.
 * The mock server is used to simulate the camera.
 */
public class IpcameraTest {

  private IPCamera camera, busyCamera;
  private CameraController cameraController = mock(CameraController.class);
  private Config config = mock(Config.class);
  private Logger logger = mock(Logger.class);
  

  
  @Before
  public final void setUp() throws InvalidCameraTypeException {
    camera = spy(new IPCamera("test", cameraController));
    busyCamera = spy(new IPCamera("test", cameraController));
    busyCamera.setBusy(true);

    when(config.getValue("IPCameraTimeOut")).thenReturn("2");
    when(cameraController.getConfig()).thenReturn(config);
    when(cameraController.getLogger()).thenReturn(logger);
    camera = spy(new IPCamera("test", cameraController));
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
  }
  
  public void setCameraBehaviour(String cmd, String response) throws IpcameraConnectionException {
    doReturn(response).when(camera).sendCommand("aw_ptz?cmd=%23" + cmd + "&res=1");
  }
  
  @Test
  public final void testGetMACAddress() throws CameraConnectionException, IOException {
    String ipcameraInfo = IOUtils.toString(new FileInputStream("resources" + File.separator + "test" + File.separator + "ipcameraInfoTest.txt"));
    doReturn(ipcameraInfo).when(camera).sendCommand("getinfo?FILE=1");
    String actual = camera.getMacAddress();
    assertEquals("8C-C1-21-F0-46-C9", actual);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetMACAddressFails() throws CameraConnectionException {
    doReturn("").when(camera).sendCommand("getinfo?FILE=1");
    camera.getMacAddress();
  }


  @Test
  public final void testMoveToHomePosition() throws CameraConnectionException, CameraBusyException {
    setCameraBehaviour("APS80008000111", "aPS80008000111");
    
    Position pos = new Position(0, 180);
    camera.moveTo(pos, 17, 1);
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23APS80008000111&res=1");
  }
  
  @Test
  public final void testMoveToWithSpeed1() throws CameraConnectionException, CameraBusyException {
    setCameraBehaviour("APS80008000011", "aPS80008000011");
    
    Position pos = new Position(0, 180);
    camera.moveTo(pos, 1, 1);
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23APS80008000011&res=1");
  }
  
  @Test
  public final void testMoveWithSpecifiedSpeed() throws CameraConnectionException, CameraBusyException {
    setCameraBehaviour("PTS0199", "pTS0199");
    camera.move(01, 99);
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23PTS0199&res=1");
  }
  
  @Test
  public final void testGetPosition() throws CameraConnectionException {
    setCameraBehaviour("APC", "aPC80008000");
    Position res = camera.getPosition();
    
    assertEquals(0, res.getPan(), 0.000001);
    assertEquals(180, res.getTilt(), 0.000001);
  }
  
  @Test
  public final void testGetPositionTwice() throws CameraConnectionException {
    setCameraBehaviour("APC", "aPC80008000");
    Position res = camera.getPosition();
    res = camera.getPosition();
    assertEquals(0, res.getPan(), 0.000001);
    assertEquals(180, res.getTilt(), 0.000001);
  }
  
  @Test
  public final void testGetPositionTwice3Seconds() throws CameraConnectionException, InterruptedException {
    setCameraBehaviour("APC", "aPC80008000");
    Position res = camera.getPosition();
    Thread.sleep(1); 
    res = camera.getPosition();
    assertEquals(0, res.getPan(), 0.000001);
    assertEquals(180, res.getTilt(), 0.000001);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetPositionException() throws CameraConnectionException {
    setCameraBehaviour("APC", "aPP80008000");
    camera.getPosition();
  }
   
  @Test
  public final void testGetStreamLink() {
    String res = camera.getStreamLink();
    assertEquals(res, "http://test/cgi-bin/mjpeg");
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testNonExcistingIpAdres() throws CameraConnectionException, CameraBusyException {
    IPCamera camera = new IPCamera("1.300.3.4", cameraController);
    camera.move(180, 50);
  }

  @Test
  public final void testGetSetId() {
    IPCamera camera = new IPCamera("1.300.3.4", cameraController);
    camera.setId(4);
    Assert.assertEquals(4, camera.getId());
  }

  @Test
  public final void testUninitializedId(){
    IPCamera camera = new IPCamera("1.300.3.4", cameraController);
    Assert.assertEquals(-1, camera.getId());
  }
  
  @Test
  public final void testNotEqualsIPAddress() {
    IPCamera camera1 = new IPCamera("12", cameraController);
    IPCamera camera2 = new IPCamera("13", cameraController);
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public final void testEquals() {
    IPCamera camera1 = new IPCamera("12", cameraController);
    IPCamera camera2 = new IPCamera("12", cameraController);
    assertEquals(camera1, camera2);
  }
  
  @Test
  public final void testNotEqualsID() {
    IPCamera camera1 = new IPCamera("12", cameraController);
    IPCamera camera2 = new IPCamera("12", cameraController);
    camera2.setId(5);
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public final void testHashCodeNotEqual() {
    IPCamera camera1 = new IPCamera("12", cameraController);
    IPCamera camera2 = new IPCamera("12", cameraController);
    camera2.setId(5);
    assertNotEquals(camera1.hashCode(), camera2.hashCode());
  }
  
  @Test
  public final void testHashCodeEqual() {
    IPCamera camera1 = new IPCamera("12", cameraController);
    IPCamera camera2 = new IPCamera("12", cameraController);
    assertEquals(camera1.hashCode(), camera2.hashCode());
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetJSONFails() throws CameraConnectionException, CameraBusyException {
    IPCamera camera = new IPCamera("12", cameraController);
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("inuse", false);
    json.put("move", true);
    json.put("iris", true);
    json.put("zoom", true);
    json.put("focus", true);
    camera.toJSON();
  }
  
  @Test
  public final void testGetJSON() throws CameraConnectionException, CameraBusyException{
    setCameraBehaviour("D1", "d11");
    setCameraBehaviour("D3", "d31");
    JSONObject json = new JSONObject();
    json.put("id", -1);
    json.put("inuse", false);
    json.put("move", true);
    json.put("zoom", true);
    json.put("focus", true);
    json.put("autofocus", true);
    json.put("iris", true);
    json.put("autoiris", true);
    json.put("busy", false);
    
    assertEquals(json, camera.toJSON());
  }
  
  @Test
  public final void testCreatePreset() throws CameraConnectionException, CameraBusyException{
    setCameraBehaviour("APC", "aPC80008000");
    setCameraBehaviour("GZ", "gz655");
    setCameraBehaviour("GF", "gfA42");
    setCameraBehaviour("D1", "d11");
    setCameraBehaviour("D3", "d31");
    setCameraBehaviour("GI", "giD421");
    IPCameraPreset expected = new IPCameraPreset(new ZoomPosition(0, 180, 256), 1261, 2029, true, true, -1, "name");
    
    assertEquals(expected, camera.createPreset(new HashSet<>(), "name"));
  }
  
  @Test
  public final void testGetIPAddress() {
    assertEquals("test", camera.getIpaddress());
  }

  @Test
  public void testIsSetInUse() {
    IPCamera camera1 = new IPCamera("ip", cameraController);
    Assert.assertFalse(camera1.isInUse());
    camera1.setInUse();
    Assert.assertTrue(camera1.isInUse());
  }

  @Test
  public void testBusyInitializedFalse() {
    IPCamera cam = spy(new IPCamera("12", cameraController));
    Assert.assertFalse(cam.isBusy());
  }

  @Test
  public void testSetBusy() {
    IPCamera cam = spy(new IPCamera("12", cameraController));
    cam.setBusy(true);
    Assert.assertTrue(cam.isBusy());
  }

  @Test (expected = CameraBusyException.class)
  public void testBusyMoveTo() throws CameraBusyException, CameraConnectionException {
    busyCamera.moveTo(new Position(3.0, 4.0), 2, 2);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusyMoveFocus() throws CameraBusyException, CameraConnectionException {
    busyCamera.moveFocus(40);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusyMoveIris() throws CameraBusyException, CameraConnectionException {
    busyCamera.moveIris(40);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusyMove() throws CameraBusyException, CameraConnectionException {
    busyCamera.move(3, 3);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusyAutoFocusOn() throws CameraBusyException, CameraConnectionException {
    busyCamera.setAutoFocusOn(true);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusyAutoIrisOn() throws CameraBusyException, CameraConnectionException {
    busyCamera.setAutoIrisOn(true);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusySetFocusPosition() throws CameraBusyException, CameraConnectionException {
    busyCamera.setFocusPosition(30);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusySetIrisPosition() throws CameraBusyException, CameraConnectionException {
    busyCamera.setIrisPosition(30);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusyZoom() throws CameraBusyException, CameraConnectionException {
    busyCamera.zoom(30);
  }

  @Test (expected = CameraBusyException.class)
  public void testBusyZoomTo() throws CameraBusyException, CameraConnectionException {
    busyCamera.zoomTo(30);
  }

}
