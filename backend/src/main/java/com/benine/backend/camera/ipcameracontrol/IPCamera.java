package com.benine.backend.camera.ipcameracontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.benine.backend.camera.Camera; 

/**
 * Class to communicate with an IP Camera.
 * @author Bryan
 */
public class IPCamera implements Camera {

  String ipaddress;

  /**
   *  Create a new IP Camera object.
   *  @param ip address of this camera.
   */
  public IPCamera(String ip) {
    ipaddress = ip;
  }
  
  /**
   * Get the URL to the stream of this camera.
   * @return URL in string format.
   */
  public String getStreamLink() {
    return "http://" + ipaddress + "/cgi-bin/mjpeg";
  }
  
  /**
   * Method to send an HTTP command to the IP address of this camera.
   * @param cmd command to send to the camera.
   * @return Response of the request.
   * @throws IpcameraConnectionException when the request fails.
   */
  public String sendCommand(String cmd) throws IpcameraConnectionException {
    String res = null;
    try {
      InputStream com = new URL("http://" + ipaddress + "/cgi-bin/aw_ptz?cmd=" + cmd + "&res=1").openStream();
      BufferedReader buf = new BufferedReader(new InputStreamReader(com));
      res = buf.readLine();
      com.close();
    } catch (IOException excep) {
      throw new IpcameraConnectionException("Sending command to camera at" + ipaddress + " failed");
    }
    
    return res;
  }
  
  

}
