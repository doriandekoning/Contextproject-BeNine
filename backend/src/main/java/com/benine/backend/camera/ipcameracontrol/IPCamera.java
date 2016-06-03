package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.BasicCamera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.PresetCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.benine.backend.preset.IPCameraPreset;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to communicate with an IP Camera from .
 */
public class IPCamera extends BasicCamera implements MovingCamera,
        IrisCamera, ZoomingCamera, FocussingCamera, PresetCamera {

  private String ipaddress;
  
  private Map<String, String> attributes = new HashMap<>();
  private Map<String, Long> timeStamps = new HashMap<>();
  private Logger logger;

  /**
   *  Create a new IP Camera object.
   *  @param ip address of this camera.
   */
  public IPCamera(String ip) {
    super(StreamType.MJPEG);
    ipaddress = ip;
    logger = ServerController.getInstance().getLogger();
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
    String res = getValue("%23APC", "aPC");
    return new Position(convertPanToDouble(res.substring(0, 4)),
                                  convertTiltToDouble(res.substring(4)));
  }
  
  /**
   * Checks if the requested value is already request from the camera in the last 2 seconds.
   * Otherwise it requests the value from the camera and saves it.
   * @param command for the requested value.
   * @param verifyResponse to verify the response from the camera.
   * @return the requested value
   * @throws IpcameraConnectionException when the requested value can not be retrieved.
   */
  private String getValue(String command, String verifyResponse)
                                                      throws IpcameraConnectionException {
    Date date = new Date();
    Config config = ServerController.getInstance().getConfig();
    int timeout = Integer.parseInt(config.getValue("IPCameraTimeOut"));
    if (timeStamps.get(command) == null || date.getTime() - timeStamps.get(command) > timeout) {
      String res = sendControlCommand(command);
      res = verifyResponse(res, verifyResponse);
      timeStamps.put(command, date.getTime());
      attributes.put(command, res);
    }
    return attributes.get(command);
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
    String res = getValue("%23GF", "gf");
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
    String response = getValue("%23D1", "d1");
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
    String response = getValue("%23D3", "d3");
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
    String response = getValue("%23GI", "gi");
    return Integer.valueOf(response.substring(0, 3), 16) - 1365;
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
    String response = getValue("%23GZ", "gz");
    return Integer.valueOf(response, 16) - 1365;
  }
  
  /**
   * Zoom to a specified position.
   * Value must be between 0 and 2730.
   * Where 0 is completely zoomed out.
   * @param zpos position to zoom to.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void zoomTo(int zpos) throws CameraConnectionException {
    zpos = Math.max(0, zpos);
    zpos = Math.min(2730, zpos);
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
    logger.log("Send command: " + cmd + " to camera: " + getId(), LogEvent.Type.INFO);
    try {
      URL url = new URL("http://" + ipaddress + "/cgi-bin/" + cmd);
      URLConnection con = url.openConnection();
      con.setConnectTimeout(1000);
      con.setReadTimeout(1000);
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
  public JSONObject toJSON() throws CameraConnectionException {
    logger.log("JSON representation requested for camera " + getId(), LogEvent.Type.INFO);
    JSONObject json = new JSONObject();
    json.put("id", this.getId());
    json.put("inuse", isInUse());
    json.put("move", true);
    json.put("zoom", true);
    json.put("focus", true);
    json.put("iris", true);
    json.put("autofocus", isAutoFocusOn());
    json.put("autoiris", isAutoIrisOn());
    return  json; 
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
      logger.log("Camera responded correctly: " + response, LogEvent.Type.INFO);
      return response.substring(expected.length());
    } else {
      logger.log("Camera response is not correct, expected: "
           + expected + ", but it was : " + response, LogEvent.Type.CRITICAL);
      throw new IpcameraConnectionException("Response of camera is not correct expected: " 
          + expected + ", but it was : " + response, getId());
    }
  }

  @Override
  public IPCameraPreset createPreset(List<String> tagList) throws CameraConnectionException {
    int zoom = getZoomPosition();
    double pan = getPosition().getPan();
    double tilt = getPosition().getTilt();
    int focus = getFocusPosition();
    int iris = getIrisPosition();
    int panspeed = 15;
    int tiltspeed = 1;
    boolean autoiris = isAutoIrisOn();
    boolean autofocus = isAutoFocusOn();
    int cameraId = getId();
    return new IPCameraPreset(new Position(pan, tilt), zoom, focus, iris, autofocus, panspeed,
            tiltspeed, autoiris, cameraId, tagList);
  }
}
