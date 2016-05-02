package com.benine.backend.cameracontrol.ipcameracontrol;

import com.benine.backend.cameracontrol.Camera;
import com.benine.backend.cameracontrol.CameraOperations;

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

public class Ipcamera extends Camera {

  String ipaddress;

  /**
   *  Create a new IP Camera object.
   *  @param ip address of this camera.
   */
  public Ipcamera(String ip) {
    ipaddress = ip;
    ArrayList<CameraOperations> list = new ArrayList<CameraOperations>();
    list.add(new IpcameraIris(this));
    list.add(new IpcameraZoom(this));
    list.add(new IpcameraFocus(this));
    setOperations(list);
  }

  /**
   * Supported range:
   * pan: -175 to 175 degrees.
   * tilt: -30 to 210 degrees.
   * pan speed: 1 to 30.
   * tilt speed: 0 to 2.
   */
  @Override
  public void moveTo(double pan, double tilt, int panSpeed, int tiltSpeed) 
                                                                throws IpcameraConnectionException {
    sendCommand("%23APS" + convertPanToHex(pan) + convertTiltToHex(tilt) 
                    + convertPanSpeedtoHex(panSpeed) + convertTiltSpeed(tiltSpeed));
  }
  
  /**
   * Values must be between 1 and 99 otherwise they will be rounded.
   * Hereby is 1 max speed to left or downward.
   * 99 is max speed to right or upward.
   */
  @Override
  public void move(int pan, int tilt) throws IpcameraConnectionException {
    pan = Math.max(1, pan);
    pan = Math.min(99, pan);
    tilt = Math.max(1, tilt);
    tilt = Math.min(99, tilt);
    NumberFormat formatter = new DecimalFormat("00");
    sendCommand("%23PTS" + formatter.format(pan) + formatter.format(tilt));
  }

  @Override
  public double[] getPosition() throws IpcameraConnectionException {
    String res = sendCommand("%23APC");
    if (res.substring(0, 3).equals("aPC")) {
      return new double[]{convertPanToDouble(res.substring(3, 7)),
                                  convertTiltToDouble(res.substring(7))};
    } else {
      throw new IpcameraConnectionException("Getting the position of the camera failed.");
    }
  }

  



  

  @Override
  public String getStreamLink() {
    return "http://" + ipaddress + "/cgi-bin/mjpeg";
  }
  
  protected String sendCommand(String cmd) throws IpcameraConnectionException {
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
    return (int)(((Integer.valueOf(tilt, 16) - 7284) / 121.3541667) + 0.5) - 30;
  }
 
  /**
   * Convert the tilt position in a range the camera supports.
   * @param tilt input position in degrees.
   * @return tilt position in hexadecimal form.
   */
  private String convertTiltToHex(double tilt) {
    tilt = Math.min(210, tilt);
    tilt = Math.max(-30, tilt);
    tilt = ((tilt + 30) * 121.3541667) + 7284;
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
    return (int)(((Integer.valueOf(pan, 16) - 11530) / 121.3628571) + 0.5) - 175;
  }
  
  /**
   * Convert the pan position in a range the camera supports.
   * @param pan input position in degrees.
   * @return pan position in hexadecimal form.
   */
  private String convertPanToHex(double pan) {
    pan = Math.min(175, pan);
    pan = Math.max(-175, pan);
    pan = ((pan + 175) * 121.3628571) + 11530;

    return Integer.toHexString((int) (pan + 0.5));
  }

}
