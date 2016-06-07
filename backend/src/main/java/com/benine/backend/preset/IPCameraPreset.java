package com.benine.backend.preset;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * IPCamera preset which stores the data to recall this preset position.
 */
public class IPCameraPreset extends Preset {

  private Position position;
  private int zoom;
  private int focus;
  private int iris;
  private boolean autofocus;
  private int panspeed;
  private int tiltspeed;
  private boolean autoiris;

  /**
   * Constructs a preset.
   *
   * @param cameraId The id of the camera associated with this preset.
   */
  public IPCameraPreset(int cameraId) {
    super(cameraId);
  }


  @Override
  public JSONObject toJSON() {
    JSONObject json = super.toJSON();

    json.put("pan", position.getPan());
    json.put("tilt", position.getTilt());
    json.put("zoom", zoom);
    json.put("focus", focus);
    json.put("iris", iris);
    json.put("autofocus", autofocus);
    json.put("panspeed", panspeed);
    json.put("tiltspeed", tiltspeed);
    json.put("autoiris", autoiris);
    return json;
  }
  
  public Position getPosition() {
    return position;
  }

  public void setPosition(Position pos) {
    this.position = pos;
  }

  public int getZoom() {
    return zoom;
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
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
   * Creates a sql query to insert a preset in the database.
   * @return The query
   */
  public String createAddSqlQuery() {
    int auto = 0;
    if (isAutofocus()) {
      auto = 1;
    }
    int autoir = 0;
    if (isAutoiris()) {
      autoir = 1;
    }
    return "INSERT INTO presetsdatabase.presets VALUES(" + getId() + ","
        + getPosition().getPan() + "," + getPosition().getTilt()
        + "," + getZoom() + "," + getFocus()
        + "," + getIris() + "," + auto + "," + getPanspeed() + ","
        + getTiltspeed() + "," + autoir + ",'" + getImage() + "',"
        + getCameraId() + ")";
  }


  /**
   * Moves the camera
   * @param camera  A Camera object.
   * @throws CameraConnectionException  If the camera cannot be reached.
   */
  @Override
  public void excecutePreset(Camera camera) throws CameraConnectionException, CameraBusyException {
    if (camera instanceof IPCamera) {
      IPCamera ipcamera = (IPCamera) camera;

      ipcamera.moveTo(getPosition(), getPanspeed(), getTiltspeed());
      ipcamera.zoomTo(getZoom());
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
    result = prime * result + zoom;
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
    } else if (!tags.equals(other.getTags())) {
      return false;
    }
    return other.zoom == this.zoom;
  }

  @Override
  public String createDeleteSQL() {
    return "DELETE FROM presets WHERE ID = " + getId();
  }

  @Override
  public String toString() {
    return "IPCameraPreset{" +
            "position=" + position +
            ", zoom=" + zoom +
            ", focus=" + focus +
            ", iris=" + iris +
            ", autofocus=" + autofocus +
            ", panspeed=" + panspeed +
            ", tiltspeed=" + tiltspeed +
            ", autoiris=" + autoiris +
            '}';
  }
}
