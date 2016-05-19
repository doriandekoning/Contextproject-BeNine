package com.benine.backend.camera;

import com.benine.backend.Preset;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dorian on 5-5-16.
 */
public class SimpleCamera implements Camera {

  private int id = -1;
  private Preset[] presetsFromCamera = new Preset[16];
  private String streamLink;

  /**
   * Creates a JSON representation of this object.
   * @return A JSON string.
   * @throws CameraConnectionException thrown when the connection with the camera can't be used.
   */
  @Override
  public String toJSON() throws CameraConnectionException {
    JSONObject object = new JSONObject();
    object.put("id", getId());
    object.put("streamlink", getStreamLink());
    return object.toString();
  }

  /**
   * Sets id.
   * @param id the new id.
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Getter for id.
   * @return the id of this camera.
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Sets the stream link for this camera.
   * @return the new stream link.
   */
  public String getStreamLink() {
    return streamLink;
  }

  /**
   * Sets streamlink
   * @param streamLink an url string pointing to a mjpeg stream.
   */
  public void setStreamLink(String streamLink) {
    this.streamLink = streamLink;
  }

  @Override
  public Preset[] getPresets() {
    Preset[] copyPresets = new Preset[presetsFromCamera.length];
    System.arraycopy(presetsFromCamera, 0, copyPresets, 0, presetsFromCamera.length);
    return copyPresets;
  }

  @Override
  public void setPresets(Preset[] presets) {
    Preset[] newPresets = new Preset[presets.length];
    System.arraycopy(presets, 0, newPresets, 0, presets.length);
    presetsFromCamera = newPresets;
  }

  @Override
  public void setPresetsFromArrayList(ArrayList<Preset> presets) {
    presetsFromCamera = new Preset[16];
    int i = 0;
    for (Preset preset : presets) {
      presetsFromCamera[i] = preset;
      i++;
    }
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + Arrays.hashCode(presetsFromCamera);
    result = prime * result + ((streamLink == null) ? 0 : streamLink.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SimpleCamera) {
      SimpleCamera that = (SimpleCamera) obj;
      if (Arrays.equals(presetsFromCamera, that.presetsFromCamera) 
          && this.id == that.id
          && (this.getStreamLink() != null && this.getStreamLink().equals(that.getStreamLink())
              || this.getStreamLink() == null && that.getStreamLink() == null)
          ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getMacAddress() throws CameraConnectionException {
    // TODO Auto-generated method stub
    return null;
  }

}
