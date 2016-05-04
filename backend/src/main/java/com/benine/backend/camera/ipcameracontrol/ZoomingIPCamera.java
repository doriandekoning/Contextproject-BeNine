package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomingCamera;

/**
 * Concrete decorator of an IP camera with functions to control the Zooming of the camera.
 * @author Bryan
 *
 */
public class ZoomingIPCamera implements ZoomingCamera {
  
  Camera camera;
  
  public ZoomingIPCamera(Camera cam) {
    camera = cam;
  }
  
  /**
   * Get the current zoom position.
   * @return the current zoom position.
   * @throws CameraConnectionException when command can not be completed.
   */
  public int getZoomPosition() throws CameraConnectionException {
    String res = sendCommand("%23GZ");
    if (res.substring(0, 2).equals("gz")) {
      return Integer.valueOf(res.substring(2), 16);
    } else {
      throw new IpcameraConnectionException("Getting the Zoom position of the camera failed.");
    }
  }
  
  /**
   * Zoom to a specified position.
   * @param zpos position to zoom to.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void zoomTo(int zpos) throws CameraConnectionException {
    zpos = Math.max(0, zpos);
    zpos = Math.min(2730, zpos);
    sendCommand("%23AXZ" + Integer.toHexString(zpos + 1365));
  }

  /**
   * Zoom with the specified speed.
   * Value between 1 and 99 where 51 is stop zoom.
   * 99 is max speed in tele direction.
   * 1 is max speed in wide direction.
   * @param dir zoom direction.
   * @throws CameraConnectionException when command can not be completed.
   */
  public void zoom(int dir) throws CameraConnectionException {
    dir = Math.max(1, dir);
    dir = Math.min(99, dir);
    sendCommand("%23Z" + dir);
  }

  @Override
  public String sendCommand(String cmd) throws CameraConnectionException {
    return camera.sendCommand(cmd);
  }

}
