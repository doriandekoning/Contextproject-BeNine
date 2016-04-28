package com.benine.ipcameracontrol;

import com.benine.Camera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * Class to communicate with an IP Camera.
 * @author Bryan
 */

public class Ipcamera implements Camera {

  String ipadres;

  /**
   *Create a new IP Camera object.
   */
  public Ipcamera(String ip) {
    ipadres = ip;
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
    }
    return null;
  }

  @Override
  public int getZoomPosition() throws IpcameraConnectionException {
    String res = sendCommand("%23GZ");
    if (res.substring(0, 2).equals("gz")) {
      return Integer.valueOf(res.substring(2), 16);
    }
    return 0;
  }
  
  /**
   * position must be between 0 and 2730.
   * Otherwise it will be rounded to the nearest supported number.
   */
  @Override
  public void zoomTo(int zpos) throws IpcameraConnectionException {
    zpos = Math.max(0, zpos);
    zpos = Math.min(2730, zpos);
    sendCommand("%23AXZ" + Integer.toHexString(zpos + 1365));
  }

  /**
   * Between 1 and 99, 1 is max speed to wide and 99 is max speed tele.
   * Otherwise it will be rounded to the nearest supported number.
   * @throws IpcameraConnectionException when command can not be send.
   */
  @Override
  public void zoom(int dir) throws IpcameraConnectionException {
    dir = Math.max(1, dir);
    dir = Math.min(99, dir);
    sendCommand("%23Z" + dir);
  }

  @Override
  public int getFocusPos() throws IpcameraConnectionException {
    String res = sendCommand("%23GF");
    if (res.substring(0, 2).equals("gf")) {
      return Integer.valueOf(res.substring(2), 16);
    }
    return 0;
  }

  /**
   * Focus position must be a number between 0 and 2730.
   * Otherwise it will be rounded to the nearest supported number.
   */
  @Override
  public void setFocusPos(int pos) throws IpcameraConnectionException {
    pos = Math.max(0, pos);
    pos = Math.min(2730, pos);
    sendCommand("%23AXF" + Integer.toHexString(pos + 1365));
  }

  /**
   * Focus speed must be between 1 and 99.
   * Otherwise it will be rounded to the nearest supported number.
   * 1: max speed near direction.
   * 99: max speed far direction.
   */
  @Override
  public void moveFocus(int speed) throws IpcameraConnectionException {
    speed = Math.max(1, speed);
    speed = Math.min(99, speed);
    sendCommand("%23F" + speed);
  }

  @Override
  public void setAutoFocusOn(boolean on) throws IpcameraConnectionException {
    if (on) {
      sendCommand("%23D11");
    } else {
      sendCommand("%23D10");
    }
  }

  @Override
  public boolean isAutoFocusOn() throws IpcameraConnectionException {
    String res = sendCommand("%23D1");
    if (res.substring(0, 2).equals("d1")) {
      if (res.substring(2).equals("1")) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void setAutoIrisOn(boolean on) throws IpcameraConnectionException {
    if (on) {
      sendCommand("%23D31");
    } else {
      sendCommand("%23D30");
    }
  }

  @Override
  public boolean isAutoIrisOn() throws IpcameraConnectionException {
    String res = sendCommand("%23D3");
    if (res.substring(0, 2).equals("d3")) {
      if (res.substring(2).equals("1")) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Iris position must be between 1 and 99.
   * 1 is close and 99 is open.
   * @throws IpcameraConnectionException 
   */
  @Override
  public void setIrisPos(int pos) throws IpcameraConnectionException {
    pos = Math.max(1, pos);
    pos = Math.min(99, pos);
    sendCommand("%23I" + pos);
  }

  @Override
  public int getIrisPos() throws IpcameraConnectionException {
    String res = sendCommand("%23GI");
    
    return Integer.valueOf(res.substring(2, 5), 16);
  }

  @Override
  public String getStreamLink() {
    return "http://" + ipadres + "/cgi-bin/mjpeg";
  }
  
  private String sendCommand(String cmd) throws IpcameraConnectionException {
    String res = null;
    try {
      InputStream com = new URL("http://" + ipadres + "/cgi-bin/aw_ptz?cmd=" + cmd + "&res=1").openStream();
      BufferedReader buf = new BufferedReader(new InputStreamReader(com));
      res = buf.readLine();
      com.close();
    } catch (IOException excep) {
      throw new IpcameraConnectionException("Sending command to camera at" + ipadres + " failed");
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
