package com.benine.backend.cameracontrol.ipcameracontrol;

import com.benine.backend.cameracontrol.CameraOperations;

public class IpcameraZoom implements CameraOperations {
  
  Ipcamera camera;
  
  public IpcameraZoom(Ipcamera cam) {
    camera = cam;
  }
  
  /**
   * Get the current zoom position.
   * @return the current zoom position.
   * @throws IpcameraConnectionException when command can not be completed.
   */
  public int getZoomPosition() throws IpcameraConnectionException {
    String res = camera.sendCommand("%23GZ");
    if (res.substring(0, 2).equals("gz")) {
      return Integer.valueOf(res.substring(2), 16);
    } else {
      throw new IpcameraConnectionException("Getting the Zoom position of the camera failed.");
    }
  }
  
  /**
   * Zoom to a specified position.
   * @param zpos position to zoom to.
   * @throws IpcameraConnectionException when command can not be completed.
   */
  public void zoomTo(int zpos) throws IpcameraConnectionException {
    zpos = Math.max(0, zpos);
    zpos = Math.min(2730, zpos);
    camera.sendCommand("%23AXZ" + Integer.toHexString(zpos + 1365));
  }

  /**
   * Zoom with the specified speed.
   * Value between 1 and 99 where 51 is stop zoom.
   * 99 is max speed in tele direction.
   * 1 is max speed in wide direction.
   * @param dir zoom direction.
   * @throws IpcameraConnectionException when command can not be completed.
   */
  public void zoom(int dir) throws IpcameraConnectionException {
    dir = Math.max(1, dir);
    dir = Math.min(99, dir);
    camera.sendCommand("%23Z" + dir);
  }

}
