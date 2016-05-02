package com.benine.ipcameracontrol;

import com.benine.FocusFunctions;

public class IpcameraFocus implements FocusFunctions {
  
  Ipcamera camera;
  
  public IpcameraFocus(Ipcamera cam) {
    camera = cam;
  }
  
  @Override
  public int getFocusPos() throws IpcameraConnectionException {
    String res = camera.sendCommand("%23GF");
    if (res.substring(0, 2).equals("gf")) {
      return Integer.valueOf(res.substring(2), 16);
    } else {
      throw new IpcameraConnectionException("Sending command to get focus position failed");
    }
  }

  /**
   * Focus position must be a number between 0 and 2730.
   * Otherwise it will be rounded to the nearest supported number.
   */
  @Override
  public void setFocusPos(int pos) throws IpcameraConnectionException {
    pos = Math.max(0, pos);
    pos = Math.min(2730, pos);
    camera.sendCommand("%23AXF" + Integer.toHexString(pos + 1365));
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
    camera.sendCommand("%23F" + speed);
  }

  @Override
  public void setAutoFocusOn(boolean on) throws IpcameraConnectionException {
    if (on) {
      camera.sendCommand("%23D11");
    } else {
      camera.sendCommand("%23D10");
    }
  }

  @Override
  public boolean isAutoFocusOn() throws IpcameraConnectionException {
    String res = camera.sendCommand("%23D1");
    if (res.substring(0, 2).equals("d1")) {
      if (res.substring(2).equals("1")) {
        return true;
      } else {
        return false;
      }
    } else {
      throw new IpcameraConnectionException("Sending command to test autofocus failed");
    }
  }
}
