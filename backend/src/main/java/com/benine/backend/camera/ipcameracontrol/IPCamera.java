package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.BasicCamera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomingCamera;

import com.benine.backend.video.StreamType;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;


/**
 * Class to communicate with an IP Camera from .
 */
public class IPCamera extends BasicCamera implements MovingCamera,
        IrisCamera, ZoomingCamera, FocussingCamera {

  private String ipaddress;

  private static final int MOVE_WAIT_DURATION = 200;

  public static final double HORIZONTAL_FOV_MIN = 3.3;
  public static final double HORIZONTAL_FOV_MAX = 60.3;
  public static final double VERTICAL_FOV_MIN = 1.9;
  public static final double VERTICAL_FOV_MAX = 36.2;

  public static final int MIN_ZOOM = 0;
  public static final int MAX_ZOOM = 2730;


  /**
   *  Create a new IP Camera object.
   *  @param ip address of this camera.
   */
  public IPCamera(String ip) {
    super(StreamType.MJPEG);
    ipaddress = ip;
  }
  
  /**
   * Supported range:
   * pan: -175 to 175 degrees.
   * tilt: -30 to 210 degrees.
   * pan speed: 1 to 30.
   * tilt speed: 0 to 2.
   * @param pos position to move to.
   * @param panSpeed integer to specify the speed of the pan movement.
   * @param tiltSpeed integer to specify the speed of the tilt movement.
   * @throws CameraConnectionException when command can not be completed.
   */
  @Override
  public void moveTo(Position pos, int panSpeed, int tiltSpeed) 
                                                                throws CameraConnectionException {
    String panSp = convertPanSpeedtoHex(panSpeed).toUpperCase();
    panSp = ("00" + panSp).substring(panSp.length());
    String res = sendControlCommand("%23APS" + convertPanToHex(pos.getPan()).toUpperCase() 
                    + convertTiltToHex(pos.getTilt()).toUpperCase()
                    + panSp
                    + convertTiltSpeed(tiltSpeed));
    verifyResponse(res, "aPS");
  }
  
  /**
   * Values must be between 1 and 99 otherwise they will be rounded.
   * Hereby is 1 max speed to left or downward.
   * 99 is max speed to right or upward.
   * @param pan movement direction over horizontal axis.
   * @param tilt movement direction over vertical axis.
   * @throws CameraConnectionException when command can not be completed.
   */
  @Override
  public void move(int pan, int tilt) throws CameraConnectionException {
    pan = Math.max(1, pan);
    pan = Math.min(99, pan);
    tilt = Math.max(1, tilt);
    tilt = Math.min(99, tilt);
    NumberFormat formatter = new DecimalFormat("00");
    String res = sendControlCommand("%23PTS" + formatter.format(pan) + formatter.format(tilt));
    verifyResponse(res, "pTS");
  }

  @Override
  public Position getPosition() throws CameraConnectionException {
    String res = sendControlCommand("%23APC");
    res = verifyResponse(res, "aPC");
    return new Position(convertPanToDouble(res.substring(0, 4)),
                                  convertTiltToDouble(res.substring(4)));
  }
  
  /**
   * Convert the pan speed to hexadecimal form.
   * And ensure it is in the range the camera supports.
   * @param pan input pan speed.
   * @return pan speed in hexadecimal form.
   */
  private String convertPanSpeedtoHex(int pan) {
    pan = Math.min(29, pan);
    pan = Math.max(0, pan);
    return Integer.toHexString(pan);
  }
  
  /**
   * Convert the hexadecimal presentation of the tilt position to degrees.
   * @param tilt position in hexadecimal.
   * @return tilt position in degrees.
   */
  private double convertTiltToDouble(String tilt) {
    return (int)((Integer.valueOf(tilt, 16) - 7284) / 121.3541667 + 0.5) - 30;
  }
 
  /**
   * Convert the tilt position in a range the camera supports.
   * @param tilt input position in degrees.
   * @return tilt position in hexadecimal form.
   */
  private String convertTiltToHex(double tilt) {
    tilt = Math.min(210, tilt);
    tilt = Math.max(-30, tilt);
    tilt = (tilt + 30) * 121.3541667 + 7284;
    return Integer.toHexString((int) (tilt + 0.5));
  }
  
  /**
   * Convert the tilt speed to be only 0, 1 or 2.
   * @param tilt input speed.
   * @return supported speed by the camera.
   */
  private int convertTiltSpeed(int tilt) {
    tilt = Math.min(2, tilt);
    tilt = Math.max(0, tilt);
    return tilt;
  }
  
  /**
   * Converts hexadecimal representation to degrees.
   * @param pan hexadecimal pan position.
   * @return pan position in degrees.
   */
  private double convertPanToDouble(String pan) {
    return (int)((Integer.valueOf(pan, 16) - 11530) / 121.3628571 + 0.5) - 175;
  }
  
  /**
   * Convert the pan position in a range the camera supports.
   * @param pan input position in degrees.
   * @return pan position in hexadecimal form.
   */
  private String convertPanToHex(double pan) {
    pan = Math.min(175, pan);
    pan = Math.max(-175, pan);
    pan = (pan + 175) * 121.3628571 + 11530;
    return Integer.toHexString((int) (pan + 0.5));
  }
  
  /**
   * Get the focus position.
   * @return focus position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getFocusPosition() throws CameraConnectionException {
    String res = sendControlCommand("%23GF");
    res = verifyResponse(res, "gf");
    return Integer.valueOf(res, 16) - 1365;
  }

  /**
   * Set the focus position
   * @param pos position of the focus to move to.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setFocusPosition(int pos) throws CameraConnectionException {
    pos = Math.max(0, pos);
    pos = Math.min(2730, pos);
    String res = sendControlCommand("%23AXF" + Integer.toHexString(pos + 1365).toUpperCase());
    verifyResponse(res, "axf");
  }

  /**
   * Move the focus in the specified direction.
   * Values between 1 and 99 where 50 is stop focusing.
   * 1 is focus nearer with max speed
   * 99 is focus further with max speed
   * @param speed value with which speed is focusing.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void moveFocus(int speed) throws CameraConnectionException {
    speed = Math.max(1, speed);
    speed = Math.min(99, speed);
    NumberFormat formatter = new DecimalFormat("00");
    String res = sendControlCommand("%23F" + formatter.format(speed));
    verifyResponse(res, "fS");
  }

  /**
   * Turn auto focus on or off.
   * @param on true for auto focus on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setAutoFocusOn(boolean on) throws CameraConnectionException {
    String response = sendControlCommand("%23D1" + Boolean.compare(on, false));
    verifyResponse(response, "d1");
  }
  
  /**
   * Request if the auto focus is on.
   * @return true if auto focus is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public boolean isAutoFocusOn() throws CameraConnectionException {
    String response = sendControlCommand("%23D1");
    response = verifyResponse(response, "d1");
    return Integer.parseInt(response) == 1;
  }
  
  /**
   * Set the control of the iris to on.
   * @param on true for auto iris on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setAutoIrisOn(boolean on) throws CameraConnectionException {
    String response = sendControlCommand("%23D3" + Boolean.compare(on, false));
    verifyResponse(response, "d3");
  }

  /**
   * Request if the auto iris is on.
   * @return true if the auto iris is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public boolean isAutoIrisOn() throws CameraConnectionException {
    String response = sendControlCommand("%23D3");
    response = verifyResponse(response, "d3");
    return Integer.parseInt(response) == 1;
  }
  
  /**
  * Set the iris position.
  * Values between 0 and 2730.
  * 0 is closed iris.
  * 2730 is open iris.
  * @param pos to set the iris to.
  * @throws CameraConnectionException when command can not be completed.
  */
  public void setIrisPosition(int pos) throws CameraConnectionException {
    pos = Math.max(0, pos);
    pos = Math.min(2730, pos);
    String response = sendControlCommand("%23AXI" + Integer.toHexString(pos + 1365).toUpperCase());
    verifyResponse(response , "axi");
  }

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getIrisPosition() throws CameraConnectionException {
    String res = sendControlCommand("%23GI");
    res = verifyResponse(res, "gi");
    return Integer.valueOf(res.substring(0, 3), 16) - 1365;
  }
  
  /**
   * Move the iris in the specified direction.
   * Values between 1 and 99 where 50 is stop moving.
   * 1 is close iris with max speed
   * 99 is open iris with max speed
   * @param speed value with which speed iris is changing.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void moveIris(int speed) throws CameraConnectionException {
    speed = Math.max(1, speed);
    speed = Math.min(99, speed);
    speed = speed - 50;
    int pos = getIrisPosition() + speed * 27;
    setIrisPosition(pos);
  }
  
  /**
   * Get the current zoom position.
   * @return the current zoom position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getZoomPosition() throws CameraConnectionException {
    String res = sendControlCommand("%23GZ");
    res = verifyResponse(res, "gz");
    return Integer.valueOf(res, 16) - 1365;
  }
  
  /**
   * Zoom to a specified position.
   * Value must be between 0 and MAX_ZOOM (2730).
   * Where 0 is completely zoomed out.
   * @param zpos position to zoom to.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void zoomTo(int zpos) throws CameraConnectionException {
    zpos = Math.max(MIN_ZOOM, zpos);
    zpos = Math.min(MAX_ZOOM, zpos);
    String res = sendControlCommand("%23AXZ" + Integer.toHexString(zpos + 1365).toUpperCase());
    verifyResponse(res, "axz");
  }

  /**
   * Zoom with the specified speed.
   * Value between 1 and 99 where 51 is stop zoom.
   * 99 is max speed in tele direction.
   * 1 is max speed in wide direction.
   * @param dir zoom direction.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void zoom(int dir) throws CameraConnectionException {
    dir = Math.max(1, dir);
    dir = Math.min(99, dir);
    NumberFormat formatter = new DecimalFormat("00");
    String res = sendControlCommand("%23Z" + formatter.format(dir));
    verifyResponse(res, "zS");
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
    String res = "";
    Logger logger = ServerController.getInstance().getLogger();
    logger.log("Send command: " + cmd + " to camera: " + getId(), LogEvent.Type.INFO);
    try {
      URL url = new URL("http://" + ipaddress + "/cgi-bin/" + cmd);
      URLConnection con = url.openConnection();
      con.setConnectTimeout(10000);
      con.setReadTimeout(10000);
      InputStream in = con.getInputStream();
      BufferedReader buf = new BufferedReader(new InputStreamReader(in, "UTF8"));
      try { 
        while (buf.ready()) {
          res = res.concat(buf.readLine()).concat(" ");
        }
        res = res.substring(0, res.length() - 1);
      } catch (IOException | StringIndexOutOfBoundsException excep) {
        throw 
          new IpcameraConnectionException("Sending command to camera at " + ipaddress 
                                                                      + " failed", getId());
      } finally {
        buf.close();
        in.close();
      }
    } catch (IOException e) {
      throw new IpcameraConnectionException("Sending command to camera at " + ipaddress 
                                                                      + " failed", getId());
    }
    return res;
  }
  
  /**
   * Send a command to the Camera to control the camera.
   * @param cmd to send.
   * @return Result of the command
   * @throws IpcameraConnectionException when command can not succeed.
   */
  public String sendControlCommand(String cmd) throws IpcameraConnectionException {
    return sendCommand("aw_ptz?cmd=" + cmd + "&res=1");
  }
  
  /**
   * Returns a JSON representation of this camera.
   * @return A JSON representation of this camera.
   */
  @Override
  public String toJSON() throws CameraConnectionException {
    Logger logger = ServerController.getInstance().getLogger();
    logger.log("JSON representation requested for camera " + getId(), LogEvent.Type.INFO);
    JSONObject json = new JSONObject();
    json.put("id", this.getId());
    json.put("inuse", isInUse());
    try {
      json.put("pan", getPosition().getPan());
      json.put("tilt", getPosition().getTilt());
      json.put("zoom", getZoomPosition());
      json.put("focus", getFocusPosition());
      json.put("autofocus", isAutoFocusOn());
      json.put("iris", getIrisPosition());
      json.put("autoiris", isAutoIrisOn());
      json.put("streamlink", getStreamLink());
    } catch (Exception e) {
      logger.log("Failed to get the JSON representation of camera: " 
                                                  + getId(), LogEvent.Type.CRITICAL);
    }
    return  json.toString();
    
  }

  /**
   * Returns the IP Address of this camera.
   * @return String IP Address
   */
  public String getIpaddress() {
    return ipaddress;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + super.hashCode();
    result = prime * result + ((ipaddress == null) ? 0 : ipaddress.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IPCamera) {
      IPCamera that = (IPCamera) obj;
      if (super.equals(that)
          && this.ipaddress.equals(that.ipaddress)
          ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getMacAddress() throws CameraConnectionException {
    String res = sendCommand("getinfo?FILE=1");
    HashMap<String, String> cameraInfo = new HashMap<String, String>();
    if (res != null) {
      cameraInfo = parseCameraInfo(res);
    }    
    if (cameraInfo.get("MAC") == null) {
      throw new IpcameraConnectionException("Getting the info of the camera at " 
                                                          + ipaddress + " failed", getId());
    }
    return cameraInfo.get("MAC");
  }

  /**
   * Parse the camera info to a hashmap.
   * @param res string of all the camera information
   * @return hashmap of the camera info.
   */
  public HashMap<String, String> parseCameraInfo(String res) {
    String[] values = res.split(" ");
    HashMap<String, String> valuesmap = new HashMap<String, String>();
    for (int i = 0; i < values.length; i++) {
      String[] pair = values[i].split("=");
      String name = pair[0];
      String value = null;
      if (pair.length > 1) {
        value = pair[1];
      }
      valuesmap.put(name, value);
    }  
    return valuesmap;
  }
  
  /**
   * Verify response from  IP camera.
   * @param response received from camera.
   * @param expected prefix of the response.
   * @return data of the response.
   * @throws IpcameraConnectionException when response is not as expected.
   */
  private String verifyResponse(String response, String expected)
                                                  throws IpcameraConnectionException {
    if (response.startsWith(expected)) {
      ServerController.getInstance().getLogger().log(
                          "Camera responded correctly: "
                          + response, LogEvent.Type.INFO);
      return response.substring(expected.length());
    } else {
      ServerController.getInstance().getLogger().log(
          "Camera response is not correct, expected: "
           + expected + ", but it was : " + response, LogEvent.Type.CRITICAL);
      throw new IpcameraConnectionException("Response of camera is not correct expected: " 
          + expected + ", but it was : " + response, getId());
    }
  }

  /**
   * Waits until the camera has arrived at a location or the timeout has expired.
   * @param pos The position the camera should be at.
   * @param zoom the zoom of the camera
   * @param timeout the timeout after which to give up waiting
   * @return true if the camera is at the specified location false otherwise
   */
  public void waitUntilAtPosition(Position pos, int zoom, long timeout)
          throws InterruptedException, CameraConnectionException, TimeoutException {
    long timedOutTime = System.currentTimeMillis() + timeout;
    do {
      if (getPosition().equals(pos) && zoom == getZoomPosition()) {
        return;
      }
      Thread.sleep(MOVE_WAIT_DURATION);
    } while (System.currentTimeMillis() < timedOutTime );
    throw new TimeoutException();
  }
}
