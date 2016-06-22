package com.benine.backend.camera;

import com.benine.backend.video.StreamType;

import org.json.simple.JSONObject;

import java.util.Date;

/**
 * Interface for communication with remote camera's.
 */
public abstract class BasicCamera implements Camera {
  
  public static final int USETIMEOUT = 7000;

  private int id;

  private StreamType streamtype;

  private boolean inUse;
  
  private long inUseTimeOut;

  private String cameraType;

  /**
   * Constructor for a Basic Camera.
   * @param streamType The streamType of this camera.
   * @param cameraType The cameraType of this camera.
   */
  public BasicCamera(String cameraType, StreamType streamType) {
    this.id = -1;
    this.streamtype = streamType;
    this.inUse = false;
    this.cameraType = cameraType;
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
   * @return ID of this camera.
   */
  public int getId() {
    return this.id;
  }

  /**
   * Get the camera type of this camera.
   * @return ID of this camera.
   */
  public String getCameraType() {
    return this.cameraType;
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
    Date date = new Date();
    if (date.getTime() > inUseTimeOut) {
      return false;
    }
    return inUse;
  }

  @Override
  public void setInUse() {
    this.inUse = true;
    Date date = new Date();
    inUseTimeOut = date.getTime() + USETIMEOUT;
  }

  @Override
  public void setNotInUse() {
    this.inUse = false;
  }
}
