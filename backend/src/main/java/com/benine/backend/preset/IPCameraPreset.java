package com.benine.backend.preset;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.video.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;


/**
 * IPCamera preset which stores the data to recall this preset position.
 */
public class IPCameraPreset extends Preset {

  private ZoomPosition position;
  private int focus;
  private int iris;
  private boolean autofocus;
  private int panspeed = 15;
  private int tiltspeed = 1;
  private boolean autoiris;
  
  /**
   * Creates a new preset based on the parameters supplied.
   * @param pos       The position of this preset.
   * @param focus     The focus of the prest
   * @param iris      The iris of the preset
   * @param autofocus The autofocus of the preset
   * @param autoiris  The autoiris of the preset
   * @param cameraId  The id of the camera associated with this preset.
   */
  public IPCameraPreset(ZoomPosition pos, int focus, int iris,
                        boolean autofocus,
                        boolean autoiris, int cameraId) {

    super(cameraId);
    this.position = pos;
    this.focus = focus;
    this.iris = iris;
    this.autofocus = autofocus;
    this.autoiris = autoiris;
  }

  /**
   * Creates a preset using the current camera parameters.
   * @param cam IPCamera to create the preset of
   * @param panSpeed the panspeed for the preset
   * @param tiltSpeed the tiltspeed of the preset
   * @throws CameraConnectionException when camera cannot be reached.
   * @throws IOException if the preset image cannot be stored.
   * @throws StreamNotAvailableException if the camera stream cannot be reached.
   * @throws CameraBusyException if the camera is busy
   */
  public IPCameraPreset(IPCamera cam, int panSpeed, int tiltSpeed)
          throws  CameraConnectionException, IOException,
                  StreamNotAvailableException, CameraBusyException {
    super(cam.getId());
    this.position = new ZoomPosition(cam.getPosition(), cam.getZoom());
    this.focus = cam.getFocusPosition();
    this.iris = cam.getIrisPosition();
    this.autoiris = cam.isAutoIrisOn();
    this.autofocus = cam.isAutoFocusOn();
    this.panspeed = panSpeed;
    this.tiltspeed = tiltSpeed;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = super.toJSON();

    json.put("pan", position.getPan());
    json.put("tilt", position.getTilt());
    json.put("zoom", position.getZoom());
    json.put("focus", focus);
    json.put("iris", iris);
    json.put("autofocus", autofocus);
    json.put("panspeed", panspeed);
    json.put("tiltspeed", tiltspeed);
    json.put("autoiris", autoiris);
    json.put("id", getId());
    json.put("cameraid", getCameraId());
    json.put("image", getImage());
    json.put("name", getName());
    JSONArray tagsJSON = new JSONArray();
    for (String tag : tags) {
      tagsJSON.add(tag);
    }
    json.put("tags", tagsJSON);
    return json;
  }
  
  public ZoomPosition getPosition() {
    return position;
  }

  public void setPosition(ZoomPosition pos) {
    this.position = pos;
  }

  public int getFocus() {
    return focus;
  }

  public void setFocus(int focus) {
    this.focus = focus;
  }

  public int getIris() {
    return iris;
  }

  public void setIris(int iris) {
    this.iris = iris;
  }

  public boolean isAutofocus() {
    return autofocus;
  }

  public void setAutofocus(boolean autofocus) {
    this.autofocus = autofocus;
  }

  public int getPanspeed() { 
    return panspeed;
  }

  public void setPanspeed(int panspeed) {
    this.panspeed = panspeed;
  }

  public int getTiltspeed() {
    return tiltspeed;
  }

  public void setTiltspeed(int tiltspeed) {
    this.tiltspeed = tiltspeed;
  }

  public boolean isAutoiris() {
    return autoiris; 
  }

  public void setAutoiris(boolean autoiris) {
    this.autoiris = autoiris;
  } 

  /**
   * Moves the camera
   * @param camera  A Camera object.
   * @throws CameraConnectionException  If the camera cannot be reached.
   * @throws CameraBusyException If the camera is busy
   */
  @Override
  public void excecutePreset(Camera camera) throws CameraConnectionException, CameraBusyException {
    if (camera instanceof IPCamera) {
      IPCamera ipcamera = (IPCamera) camera;

      ipcamera.moveTo(getPosition(), getPanspeed(), getTiltspeed());
      ipcamera.zoomTo(getPosition().getZoom());
      ipcamera.setAutoFocusOn(isAutofocus());
      ipcamera.setAutoIrisOn(isAutoiris());
      ipcamera.moveFocus(getFocus());  
      ipcamera.setIrisPosition(getIris());
    } else {
      throw new CameraConnectionException("Camera cannot be controller over IP: ", camera.getId());
    }
  }

  /**
   * Removes a keyword from this preset.
   * @param tag the keyword to remove
   */
  public void removeTag(String tag) {
    tags.remove(tag);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (autofocus ? 1231 : 1237);
    result = prime * result + (autoiris ? 1231 : 1237);
    result = prime * result + focus;
    result = prime * result + iris;
    result = prime * result + panspeed;
    result = prime * result + ((position == null) ? 0 : position.hashCode());
    result = prime * result + tiltspeed;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof IPCameraPreset)) {
      return false;
    }
    IPCameraPreset other = (IPCameraPreset) obj;
    if (autofocus != other.autofocus) {
      return false;
    }
    if (autoiris != other.autoiris) {
      return false;
    }
    if (focus != other.focus) {
      return false;
    }
    if (iris != other.iris) {
      return false;
    }
    if (panspeed != other.panspeed) {
      return false;
    }
    if (position == null) {
      if (other.position != null) {
        return false;
      }
    } else if (!position.equals(other.position)) {
      return false;
    }
    if (tiltspeed != other.tiltspeed) {
      return false;
    }
    if (tags == null && other.getTags() != null) {
      return false;
    } else if (tags != null && !tags.equals(other.getTags())) {
      return false;
    }
    return true;
  }

}
