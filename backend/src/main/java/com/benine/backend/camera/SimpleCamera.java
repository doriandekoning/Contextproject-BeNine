package com.benine.backend.camera;

import com.benine.backend.Preset;
import org.json.simple.JSONObject;

import java.util.ArrayList;

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
    Preset[] result = new Preset[16];
    for (int i = 0; i < presetsFromCamera.length; i++) {
      result[i] = presetsFromCamera[i];
    }
    return result;
  }

  @Override
  public void setPresets(Preset[] presets) {
    for (int i = 0; i < presets.length; i++) {
      presetsFromCamera[i] = presets[i];
    }
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
}
