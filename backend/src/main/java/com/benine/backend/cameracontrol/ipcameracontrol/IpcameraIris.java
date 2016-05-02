package com.benine.backend.cameracontrol.ipcameracontrol;

import com.benine.backend.cameracontrol.IrisFunctions;

/**
 * Ip camera Iris class for functions of the iris in IP Camera's.
 * @author Bryan
 *
 */
public class IpcameraIris implements IrisFunctions {
  
  Ipcamera camera;
  
  public IpcameraIris(Ipcamera cam) {
    camera = cam;
  }
  
  @Override
  public void setAutoIrisOn(boolean on) throws IpcameraConnectionException {
    if (on) {
      camera.sendCommand("%23D31");
    } else {
      camera.sendCommand("%23D30");
    }
  }

  @Override
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
   * Iris position must be between 1 and 99.
   * 1 is close and 99 is open.
   */
  @Override
  public void setIrisPos(int pos) throws IpcameraConnectionException {
    pos = Math.max(1, pos);
    pos = Math.min(99, pos);
    camera.sendCommand("%23I" + pos);
  }

  @Override
  public int getIrisPos() throws IpcameraConnectionException {
    String res = camera.sendCommand("%23GI");
    
    return Integer.valueOf(res.substring(2, 5), 16);
  }

}
