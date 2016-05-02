package com.benine.ipcameracontrol;

import com.benine.ZoomFunctions;

public class IpcameraZoom implements ZoomFunctions {
  
  Ipcamera camera;
  
  public IpcameraZoom(Ipcamera cam) {
    camera = cam;
  }
  
  @Override
  public int getZoomPosition() throws IpcameraConnectionException {
    String res = camera.sendCommand("%23GZ");
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
    camera.sendCommand("%23AXZ" + Integer.toHexString(zpos + 1365));
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
    camera.sendCommand("%23Z" + dir);
  }

}
