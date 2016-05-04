package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.FocussingCamera;

public class FocussingIPCamera implements FocussingCamera {
  
  Camera camera;
  
  public FocussingIPCamera(Camera cam) {
    camera = cam;
  }
  
  /**
   * Get the focus position.
   * @return focus position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getFocusPos() throws CameraConnectionException {
    String res = sendCommand("%23GF");
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
  public void setFocusPos(int pos) throws CameraConnectionException {
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
    String res = sendCommand("%23D1");
    if (res.substring(0, 2).equals("d1")) {
      if (res.substring(2).equals("1")) {
        return true;
      } else {
        return false;
      }
    } else {
      throw new CameraConnectionException("Sending command to test autofocus failed");
    }
  }

  @Override
  public String sendCommand(String cmd) throws CameraConnectionException {
    return camera.sendCommand(cmd);
  }
}
