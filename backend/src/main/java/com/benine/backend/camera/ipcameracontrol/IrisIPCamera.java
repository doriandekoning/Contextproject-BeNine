package com.benine.backend.cameracontrol.ipcameracontrol;

import com.benine.backend.cameracontrol.Camera;
import com.benine.backend.cameracontrol.CameraConnectionException;
import com.benine.backend.cameracontrol.IrisCamera;

/**
 * IP camera Iris class for functions of the iris in IP Camera's.
 * @author Bryan
 */
public class IrisIPCamera implements IrisCamera {
  
  Camera camera;
  
  public IrisIPCamera(Camera cam) {
    camera = cam;
  }
  
  /**
   * Set the control of the iris to on.
   * @param on true for auto iris on.
   * @throws IpcameraConnectionException when command can not be completed.
   */
  public void setAutoIrisOn(boolean on) throws CameraConnectionException {
    if (on) {
      sendCommand("%23D31");
    } else {
      camera.sendCommand("%23D30");
    }
  }

  /**
   * Request if the auto iris is on.
   * @return true if the auto iris is on.
   * @throws IpcameraConnectionException when command can not be completed.
   */
  public boolean isAutoIrisOn() throws CameraConnectionException {
    String res = sendCommand("%23D3");
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
  * @throws IpcameraConnectionException when command can not be completed.
  */
  public void setIrisPos(int pos) throws CameraConnectionException {
    pos = Math.max(1, pos);
    pos = Math.min(99, pos);
    sendCommand("%23I" + pos);
  }

  /**
   * Get the current iris position.
   * @return the current iris position.
   * @throws IpcameraConnectionException when command can not be completed.
   */
  public int getIrisPos() throws CameraConnectionException {
    String res = sendCommand("%23GI");
    
    return Integer.valueOf(res.substring(2, 5), 16);
  }

  @Override
  public String sendCommand(String cmd) throws CameraConnectionException {
    return camera.sendCommand(cmd);
  }

}
