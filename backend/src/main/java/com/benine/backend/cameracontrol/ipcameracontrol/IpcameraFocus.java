package com.benine.backend.cameracontrol.ipcameracontrol;

import com.benine.backend.cameracontrol.CameraAttribute;
import com.benine.backend.cameracontrol.CameraConnectionException;

public class IpcameraFocus implements CameraAttribute {
  
  Ipcamera camera;
  
  public IpcameraFocus(Ipcamera cam) {
    camera = cam;
  }
  
  /**
   * Get the focus position.
   * @return focus position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getFocusPos() throws IpcameraConnectionException {
    String res = camera.sendCommand("%23GF");
    if (res.substring(0, 2).equals("gf")) {
      return Integer.valueOf(res.substring(2), 16);
    } else {
      throw new IpcameraConnectionException("Sending command to get focus position failed");
    }
  }

  /**
   * Set the focus position
   * @param pos position of the focus to move to.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setFocusPos(int pos) throws IpcameraConnectionException {
    pos = Math.max(0, pos);
    pos = Math.min(2730, pos);
    camera.sendCommand("%23AXF" + Integer.toHexString(pos + 1365));
  }

  /**
   * Move the focus in the specified direction.
   * Values between 1 and 99 where 50 is stop focusing.
   * 1 is focus nearer with max speed
   * 99 is focus further with max speed
   * @param speed value with which speed is focusing.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void moveFocus(int speed) throws IpcameraConnectionException {
    speed = Math.max(1, speed);
    speed = Math.min(99, speed);
    camera.sendCommand("%23F" + speed);
  }

  /**
   * Turn auto focus on or off.
   * @param on true for auto focus on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setAutoFocusOn(boolean on) throws IpcameraConnectionException {
    if (on) {
      camera.sendCommand("%23D11");
    } else {
      camera.sendCommand("%23D10");
    }
  }
  
  /**
   * Request if the auto focus is on.
   * @return true if auto focus is on.
   * @throws CameraConnectionException when command can not be completed.
   */
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
