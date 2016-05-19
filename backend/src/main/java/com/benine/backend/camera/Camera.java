package com.benine.backend.camera;


import com.benine.backend.Preset;
import com.benine.backend.video.StreamType;

import java.util.ArrayList;

/**
 * Interface for communication with remote camera's.
 * @author Bryan
 */
public abstract class Camera {

  private int id;

  private StreamType streamtype;

  private Preset[] presetsFromCamera;

  public Camera(StreamType type) {
    this.id = -1;
    this.streamtype = type;
    this.presetsFromCamera = new Preset[16];
  }


  /**
   * Method to create a json object describinding the camera.
   * @return Json object in the form of a string.
   * @throws CameraConnectionException When the information can not be retrieved.
   */
  abstract public String toJSON() throws CameraConnectionException;

  /**
   * Set the ID of this camera.
   * @param id to set.
   */
  public void setId(int id) {
    this.id = id;
  };

  /**
   * Get the ID of this camera.
   * @return ID of this camra.
   */
  public int getId() {
    return this.id;
  };


  /**
   * Get the Stream Type of this camera.
   * @return StreamType ENUM of this camera.
   */
  public StreamType getStreamType() {
    return this.streamtype;
  };

  /**
   * Get the list of presets from this camera.
   * @return The preset array
   */
  public Preset[] getPresets() {
    Preset[] copyPresets = new Preset[presetsFromCamera.length];
    System.arraycopy(presetsFromCamera, 0, copyPresets, 0, presetsFromCamera.length);
    return copyPresets;
  }

  /**
   * Set the list of presets from this camera.
   * @param presets The preset array
   */
  public void setPresets(Preset[] presets) {
    Preset[] copyPresets = new Preset[presets.length];
    System.arraycopy(presets, 0, copyPresets, 0, presets.length);
    presetsFromCamera = copyPresets;
  }

  /**
   * Set the list of presets from this camera of an arraylist.
   * @param presets The ArrayList of presets
   */
  public void setPresetsFromArrayList(ArrayList<Preset> presets) {
    presetsFromCamera = new Preset[16];
    int i = 0;
    for (Preset preset : presets) {
      presetsFromCamera[i] = preset;
      i++;
    }
  }
}
