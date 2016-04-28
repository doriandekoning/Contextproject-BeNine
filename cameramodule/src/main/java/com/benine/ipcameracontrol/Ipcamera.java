package com.benine.ipcameracontrol;

import com.benine.Camera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


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

  @Override
  public void moveTo(double pan, double tilt, int panSpeed, int tiltSpeed) {
    sendCommand("%23APS" + convertPanToHex(pan) + convertTiltToHex(tilt) 
                    + convertPanSpeedtoHex(panSpeed) + convertTiltSpeed(tiltSpeed));
  }

  @Override
  public void move(int pan, int tilt) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public double[] getPosition() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getZoomPosition() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void zoomTo(int zPos) {
    // TODO Auto-generated method stub
  }

  @Override
  public void zoom(int dir) {
    // TODO Auto-generated method stub
  }

  @Override
  public int getFocusPos() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setFocusPos(int pos) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void moveFocus(int speed) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setAutoFocusOn(boolean on) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isAutoFocusOn() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setAutoIrisOn(boolean on) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isAutoIrisOn() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setIrisPos(int pos) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getIrisPos() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getStreamLink() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private String sendCommand(String cmd) {
    String res = null;
    try {
      InputStream com = new URL("http://" + ipadres + "/cgi-bin/aw_ptz?cmd=" + cmd + "&res=1").openStream();
      BufferedReader buf = new BufferedReader(new InputStreamReader(com));
      res = buf.readLine();
      com.close();
    } catch (IOException excep) {
      excep.printStackTrace();
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
