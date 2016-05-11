package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomingCamera;

import com.benine.backend.database.DatabasePreset;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


/**
 * Class to communicate with an IP Camera.
 * @author Bryan
 */
public class IPCamera implements Camera, MovingCamera, IrisCamera, ZoomingCamera, FocussingCamera {

  String ipaddress;

  private int id = -1;

  DatabasePreset[] presetsFromCamera;

  /**
   *  Create a new IP Camera object.
   *  @param ip address of this camera.
   */
  public IPCamera(String ip) {
    ipaddress = ip;
    presetsFromCamera = new DatabasePreset[16];
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
    CameraController.logger.log("Move IP camera", LogEvent.Type.INFO);
    sendCommand("%23APS" + convertPanToHex(pos.getPan()) + convertTiltToHex(pos.getTilt()) 
                    + convertPanSpeedtoHex(panSpeed) + convertTiltSpeed(tiltSpeed));
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
    CameraController.logger.log("Move IP camera with specified speed.", LogEvent.Type.INFO);
    pan = Math.max(1, pan);
    pan = Math.min(99, pan);
    tilt = Math.max(1, tilt);
    tilt = Math.min(99, tilt);
    NumberFormat formatter = new DecimalFormat("00");
    sendCommand("%23PTS" + formatter.format(pan) + formatter.format(tilt));
  }

  @Override
  public Position getPosition() throws CameraConnectionException {
    CameraController.logger.log("Get the position of the IP camera.", LogEvent.Type.INFO);
    String res = sendCommand("%23APC");
    if (res.substring(0, 3).equals("aPC")) {
      return new Position(convertPanToDouble(res.substring(3, 7)),
                                  convertTiltToDouble(res.substring(7)));
    } else {
      throw new IpcameraConnectionException("Getting the position of the camera failed.");
    }
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
  public int getFocusPos() throws CameraConnectionException {
    String res = sendCommand("%23GF");
    if (res.substring(0, 2).equals("gf")) {
      CameraController.logger.log("Get focus position of the IP Camera.", LogEvent.Type.INFO);
      return Integer.valueOf(res.substring(2), 16);
    } else {
      CameraController.logger.log("Getting the focus position failed", LogEvent.Type.CRITICAL);
      throw new IpcameraConnectionException("Sending command to get focus position failed");
    }
  }

  /**
   * Set the focus position
   * @param pos position of the focus to move to.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setFocusPos(int pos) throws CameraConnectionException {
    CameraController.logger.log("Set focus position camera.", LogEvent.Type.INFO);
    pos = Math.max(0, pos);
    pos = Math.min(2730, pos);
    sendCommand("%23AXF" + Integer.toHexString(pos + 1365));
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
    CameraController.logger.log("Move focus IP camera.", LogEvent.Type.INFO);
    speed = Math.max(1, speed);
    speed = Math.min(99, speed);
    sendCommand("%23F" + speed);
  }

  /**
   * Turn auto focus on or off.
   * @param on true for auto focus on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setAutoFocusOn(boolean on) throws CameraConnectionException {
    CameraController.logger.log("Set auto focus: " + on, LogEvent.Type.INFO);
    if (on) {
      sendCommand("%23D11");
    } else {
      sendCommand("%23D10");
    }
  }
  
  /**
   * Request if the auto focus is on.
   * @return true if auto focus is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public boolean isAutoFocusOn() throws CameraConnectionException {
    CameraController.logger.log("Checking autofocus failed.", LogEvent.Type.INFO);
    String res = sendCommand("%23D1");
    if (res.substring(0, 2).equals("d1")) {
      if (res.substring(2).equals("1")) {
        return true;
      } else {
        return false;
      }
    } else {
      CameraController.logger.log("Changing auto focus failed.", LogEvent.Type.CRITICAL);
      throw new CameraConnectionException("Sending command to test autofocus failed");
    }
  }
  
  /**
   * Set the control of the iris to on.
   * @param on true for auto iris on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setAutoIrisOn(boolean on) throws CameraConnectionException {
    CameraController.logger.log("Changing auto iris.", LogEvent.Type.INFO);
    if (on) {
      sendCommand("%23D31");
    } else {
      sendCommand("%23D30");
    }
  }

  /**
   * Request if the auto iris is on.
   * @return true if the auto iris is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public boolean isAutoIrisOn() throws CameraConnectionException {
    CameraController.logger.log("Checking auto iris.", LogEvent.Type.INFO);
    String res = sendCommand("%23D3");
    if (res.substring(0, 2).equals("d3")) {
      if (res.substring(2).equals("1")) {
        return true;
      }
    } else {
      CameraController.logger.log("Changing auto iris.", LogEvent.Type.CRITICAL);
      throw new IpcameraConnectionException(
          "Sending the message to test if auto iris is on to camera failed");
    }
    return false;
  }
  
  /**
  * Set the iris position.
  * Values between 1 and 99.
  * 1 is closed iris.
  * 99 is open iris.
  * @param pos to set the iris to.
  * @throws CameraConnectionException when command can not be completed.
  */
  public void setIrisPos(int pos) throws CameraConnectionException {
    pos = Math.max(1, pos);
    pos = Math.min(99, pos);
    sendCommand("%23I" + pos);
  }

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getIrisPos() throws CameraConnectionException {
    CameraController.logger.log("Get iris position.", LogEvent.Type.INFO);
    String res = sendCommand("%23GI");
    return Integer.valueOf(res.substring(2, 5), 16);
  }
  
  /**
   * Get the current zoom position.
   * @return the current zoom position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getZoomPosition() throws CameraConnectionException {
    CameraController.logger.log("Get zoom position.", LogEvent.Type.INFO);
    String res = sendCommand("%23GZ");
    if (res.substring(0, 2).equals("gz")) {
      return Integer.valueOf(res.substring(2), 16);
    } else {
      CameraController.logger.log("Changing zoom position failed.", LogEvent.Type.CRITICAL);
      throw new IpcameraConnectionException("Getting the Zoom position of the camera failed.");
    }
  }
  
  /**
   * Zoom to a specified position.
   * @param zpos position to zoom to.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void zoomTo(int zpos) throws CameraConnectionException {
    CameraController.logger.log("Zoom to zoom position.", LogEvent.Type.INFO);
    zpos = Math.max(0, zpos);
    zpos = Math.min(2730, zpos);
    sendCommand("%23AXZ" + Integer.toHexString(zpos + 1365));
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
    sendCommand("%23Z" + dir);
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
  
  /**
   * Returns a JSON representation of this camera.
   * @return A JSON representation of this camera.
   */
  @Override
  public String toJSON() throws CameraConnectionException {
    JSONObject json = new JSONObject();
    json.put("id", Integer.valueOf(this.id));
    try {
      json.put("pan", new Double(getPosition().getPan()));
      json.put("tilt", new Double(getPosition().getTilt()));
      json.put("zoom", new Double(getZoomPosition()));
      json.put("focus", new Double(getFocusPos()));
      json.put("autofocus", Boolean.valueOf(isAutoFocusOn()));
      json.put("iris", new Double(getIrisPos()));
      json.put("autoiris", Boolean.valueOf(isAutoIrisOn()));
      json.put("videostream", Boolean.valueOf(getStreamLink()));
    } catch (Exception e) {
      //TODO log not possible yet because logger acts funny when used in multiple threads (httpha
      System.out.println(e.toString());
    }
    return  json.toString();

  }
  
  /**
   * Sets the id of this camera.
   * @param id the id of this camera
   */
  public void setId(int id) {
    this.id = id;
  }
  
  /**
   * Gets the id of this camera.
   * @return the id of this camera
   */
  public int getId() {
    return this.id;
  }

  @Override
  public DatabasePreset[] getPresets() {
    return presetsFromCamera;
  }

  @Override
  public void setPresets(DatabasePreset[] presets) {
    presetsFromCamera = presets;
  }

  @Override
  public void setPresetsFromArrayList(ArrayList<DatabasePreset> presets) {
    presetsFromCamera = new DatabasePreset[16];
    int i = 0;
    for(DatabasePreset preset : presets) {
      presetsFromCamera[i] = preset;
      i++;
    }
  }


}
