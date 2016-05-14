package com.benine.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.doAnswer;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.camera.ipcameracontrol.IpcameraConnectionException;
import com.benine.backend.http.HttpController;


/**
 * Test class to test the interaction with a camera using the user interface.
 * This test has to be done manually as this starts only the server.
 */
public class CameraControlTest {
  
  private Config mainConfig;
  private CameraController cameraController;
  private String address;
  private int port;
  private Logger logger;

  /**
   * Set up the mocked camera and add it to the camera controller.
   * @throws CameraConnectionException when the camera can not be created.
   */
  @Before
  public void setup() throws CameraConnectionException{
    mainConfig = Main.getConfig();

    logger = mock(Logger.class);
    
    address = mainConfig.getValue("serverip");
    port = Integer.parseInt(mainConfig.getValue("serverport"));
    // Setup camerahandler
    cameraController = new CameraController();
      
    IPCamera mockcamera = mock(IPCamera.class);
    when(mockcamera.getStreamLink()).thenReturn("http://83.128.144.84:88/cgi-bin/CGIProxy.fcgi?cmd=snapPicture&usr=user&pwd=geheim");
    when(mockcamera.getId()).thenReturn(1);
    when(mockcamera.getFocusPosition()).thenReturn(40);
    when(mockcamera.getIrisPosition()).thenReturn(60);
    when(mockcamera.getPosition()).thenReturn(new Position(10, 20));
    when(mockcamera.getZoomPosition()).thenReturn(30);
    
    //Define which command to send when zoomTo is called on mocked camera.
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws IpcameraConnectionException {
          Object[] args = invocation.getArguments();
          int zoom = (int) args[0];
          if (zoom > 60) {
            sendCommand("zoomIn", "83.128.144.84:88");
          } else if( zoom < 40) {
            sendCommand("zoomOut", "83.128.144.84:88");
          } else {
            sendCommand("zoomStop", "83.128.144.84:88");
          }
          return null;
      }
    }).when(mockcamera).zoomTo(any(Integer.class));
    
    //Define which command to send when move is called on mocked camera.
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws IpcameraConnectionException {
          Object[] args = invocation.getArguments();
          int pan = (int) args[0];
          int tilt = (int) args[1];
          if (pan > 85) {
            sendCommand("ptzMoveRight", "83.128.144.84:88");
          } else if( pan < 15) {
            sendCommand("ptzMoveLeft", "83.128.144.84:88");
          } else if( tilt > 85) {
            sendCommand("ptzMoveUp", "83.128.144.84:88");
          } else if( tilt < 15) {
            sendCommand("ptzMoveDown", "83.128.144.84:88");
          } else {
            sendCommand("ptzStopRun", "83.128.144.84:88");
          }
          return null;
      }
    }).when(mockcamera).move(any(Integer.class), any(Integer.class));
    
    when(mockcamera.toJSON()).thenReturn(createJSON());
    cameraController.addCamera(mockcamera);
    
    //Add two extra camera's.
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setStreamLink(mainConfig.getValue("camera1"));
    SimpleCamera camera3 = new SimpleCamera();
    camera3.setStreamLink("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=2&scale=100&buffer=100");
    SimpleCamera camera4 = new SimpleCamera();
    camera4.setStreamLink("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=1&scale=100&buffer=100");
    SimpleCamera camera5 = new SimpleCamera();
    camera5.setStreamLink(mainConfig.getValue("camera1"));
    SimpleCamera camera6 = new SimpleCamera();
    camera6.setStreamLink("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=2&scale=100&buffer=100");
    SimpleCamera camera7 = new SimpleCamera();
    camera7.setStreamLink("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=1&scale=100&buffer=100");
    cameraController.addCamera(camera2);
    cameraController.addCamera(camera3);
    cameraController.addCamera(camera4);
    cameraController.addCamera(camera5);
    cameraController.addCamera(camera6);
    cameraController.addCamera(camera7);
  }
  
  /**
   * This methods starts a server used to manually test the user interface.
   * @throws InterruptedException when the thread is interrupted.
   * @throws IOException when the server can not be created.
   */
  @Test
  public void ManualTestUI() throws InterruptedException, IOException{
    HttpController httpController = new HttpController(address, port, logger, cameraController);

    while(true){
      Thread.sleep(100);
    }
  }
  
  /**
   * Create a Json representation of the mocked camera.
   * @return JSON in string format.
   */
  public String createJSON(){
    JSONObject json = new JSONObject();
    json.put("id", 1);
    try {
      json.put("pan", 10);
      json.put("tilt", 20);
      json.put("zoom", 30);
      json.put("focus", 40);
      json.put("autofocus", true);
      json.put("iris", 60);
      json.put("autoiris", false);
      json.put("streamlink", "http://83.128.144.84:88/cgi-bin/CGIStream.cgi?cmd=GetMJStream&usr=user&pwd=geheim");
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return json.toString();
  }
  
  /**
   * Method to send an HTTP command to the IP address of this camera.
   * @param cmd command to send to the camera.
   * @return Response of the request.
   * @throws IpcameraConnectionException when the request fails.
   */
  public String sendCommand(String cmd, String ipaddress) throws IpcameraConnectionException {
    String res = null;
    try {
      InputStream com = new URL("http://" + ipaddress + "/cgi-bin/CGIProxy.fcgi?cmd=" + cmd + "&usr=user&pwd=geheim").openStream();
      try {
        BufferedReader buf = new BufferedReader(new InputStreamReader(
            com, "UTF8"));
        res = buf.readLine();
        com.close();
      } catch (IOException excep) {
        throw 
          new IpcameraConnectionException("Sending command to camera at" + ipaddress + " failed", 0);
      } finally {
        com.close();
      }
    } catch (IOException e) {
      throw new IpcameraConnectionException("Sending command to camera at" + ipaddress + " failed", 0);
    }
    
    return res;
  }
}
