package com.benine.backend.cameracontrol.ipcameracontrol;

import com.benine.backend.cameracontrol.CameraAttribute;
import com.benine.backend.cameracontrol.CameraConnectionException;

/**
 * IP camera Iris class for functions of the iris in IP Camera's.
 * @author Bryan
 *
 */
public class IpcameraIris implements CameraAttribute {
  
  Ipcamera camera;
  
  public IpcameraIris(Ipcamera cam) {
    camera = cam;
  }
  
  /**
   * Set the control of the iris to on.
   * @param on true for auto iris on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void setAutoIrisOn(boolean on) throws IpcameraConnectionException {
    if (on) {
      camera.sendCommand("%23D31");
    } else {
      camera.sendCommand("%23D30");
    }
  }

  /**
   * Request if the auto iris is on.
   * @return true if the auto iris is on.
   * @throws CameraConnectionException when command can not be completed.
   */
  public boolean isAutoIrisOn() throws IpcameraConnectionException {
    String res = camera.sendCommand("%23D3");
    if (res.substring(0, 2).equals("d3")) {
      if (res.substring(2).equals("1")) {
        return true;
      }
    } else {
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
  public void setIrisPos(int pos) throws IpcameraConnectionException {
    pos = Math.max(1, pos);
    pos = Math.min(99, pos);
    camera.sendCommand("%23I" + pos);
  }

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getIrisPos() throws IpcameraConnectionException {
    String res = camera.sendCommand("%23GI");
    
    return Integer.valueOf(res.substring(2, 5), 16);
  }

}
