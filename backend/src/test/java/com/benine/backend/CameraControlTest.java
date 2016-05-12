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
import com.benine.backend.http.CameraInfoHandler;
import com.benine.backend.http.FocussingHandler;
import com.benine.backend.http.IrisHandler;
import com.benine.backend.http.MovingHandler;
import com.benine.backend.http.PresetHandler;
import com.benine.backend.http.ZoomingHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Test class to test the interaction with a camera using the user interface.
 * This test has to be done manually as this starts only the server.
 */
public class CameraControlTest {
  
  private Config mainConfig;
  private CameraController cameraController;
  private InetSocketAddress address;
  
  /**
   * Set up the mocked camera and add it to the camera controller.
   * @throws CameraConnectionException when the camera can not be created.
   */
  @Before
  public void setup() throws CameraConnectionException{
    mainConfig = Main.getConfig();
    
    address = new InetSocketAddress(mainConfig.getValue("serverip"), 
                                            Integer.parseInt(mainConfig.getValue("serverport")));
    // Setup camerahandler
    cameraController = new CameraController();
      
    IPCamera mockcamera = mock(IPCamera.class);
    when(mockcamera.getStreamLink()).thenReturn("http://83.128.144.84:88/cgi-bin/CGIProxy.fcgi?cmd=snapPicture&usr=user&pwd=geheim");
    when(mockcamera.getId()).thenReturn(1);
    when(mockcamera.getFocusPos()).thenReturn(40);
    when(mockcamera.getIrisPos()).thenReturn(60);
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
    cameraController.addCamera(camera2);
    SimpleCamera camera3 = new SimpleCamera();
    camera3.setStreamLink(mainConfig.getValue("camera1"));
    cameraController.addCamera(camera3);
  }
  
  /**
   * This methods starts a server used to manually test the user interface.
   * @throws InterruptedException when the thread is interrupted.
   * @throws IOException when the server can not be created.
   */
  @Test
  public void ManualTestUI() throws InterruptedException, IOException{
    
    HttpServer server = HttpServer.create(address, 10);
    server.createContext("/getCameraInfo", new CameraInfoHandler(cameraController));
    server.createContext("/focus", new FocussingHandler(cameraController));
    server.createContext("/iris", new IrisHandler(cameraController));
    server.createContext("/move", new MovingHandler(cameraController));
    server.createContext("/zoom", new ZoomingHandler(cameraController));
    server.createContext("/preset", new PresetHandler(cameraController));

    server.start();
    while (true) {
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
          new IpcameraConnectionException("Sending command to camera at" + ipaddress + " failed");
      } finally {
        com.close();
      }
    } catch (IOException e) {
      throw new IpcameraConnectionException("Sending command to camera at" + ipaddress + " failed");
    }
    
    return res;
  }
}
