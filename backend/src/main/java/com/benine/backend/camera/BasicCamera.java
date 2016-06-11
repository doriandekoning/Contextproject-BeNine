package com.benine.backend.camera;

import com.benine.backend.video.StreamType;

import org.json.simple.JSONObject;

/**
 * Interface for communication with remote camera's.
 */
public abstract class BasicCamera implements Camera {

  private int id;

  private StreamType streamtype;

  private boolean inUse;

  /**
   * Constructor for a Basic Camera.
   * @param type The streamType of this camera.
   */
  public BasicCamera(StreamType type) {
    this.id = -1;
    this.streamtype = type;
    this.inUse = false;
  }

  /**
   * Method to create a json object describinding the camera.
   * @return Json object in the form of a string.
   * @throws CameraConnectionException When the information can not be retrieved.
   * @throws CameraBusyException if the camera is busy
   */
  public abstract JSONObject toJSON() throws CameraConnectionException, CameraBusyException;

  /**
   * Set the ID of this camera.
   * @param id to set.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get the ID of this camera.
   * @return ID of this camra.
   */
  public int getId() {
    return this.id;
  }


  /**
   * Get the Stream Type of this camera.
   * @return StreamType ENUM of this camera.
   */
  public StreamType getStreamType() {
    return this.streamtype;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((streamtype == null) ? 0 : streamtype.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BasicCamera) {
      BasicCamera that = (BasicCamera) obj;
      if (this.getId() == that.getId()
          && this.streamtype.equals(that.streamtype)
          ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isInUse() {
    return inUse;
  }

  @Override
  public void setInUse() {
    this.inUse = true;
  }

  @Override
  public void setNotInUse() {
    this.inUse = false;
  }
}
