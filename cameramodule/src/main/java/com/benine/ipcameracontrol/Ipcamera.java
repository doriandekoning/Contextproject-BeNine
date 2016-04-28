package com.benine.ipcameracontrol;

import com.benine.Camera;

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
    // TODO Auto-generated method stub
    
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

}
