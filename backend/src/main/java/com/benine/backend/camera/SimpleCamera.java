package com.benine.backend.camera;

import com.benine.backend.preset.Preset;
import com.benine.backend.preset.SimplePreset;
import com.benine.backend.video.StreamType;

import org.json.simple.JSONObject;

import java.util.Set;

/**
 * Created on 5-5-16.
 */
public class SimpleCamera extends BasicCamera implements PresetCamera {

  private String streamLink;
  private String mACAddress;

  /**
   * Defines a simple camera, which cannot be controlled.
   */
  public SimpleCamera() {
    super("simplecamera", StreamType.MJPEG);
  }

  /**
   * Creates a JSON representation of this object.
   * @return A JSON string.
   * @throws CameraConnectionException thrown when the connection with the camera can't be used.
   */
  @Override
  public JSONObject toJSON() throws CameraConnectionException {
    JSONObject object = new JSONObject();
    object.put("id", getId());
    object.put("type", getCameraType());
    object.put("address", getStreamLink());
    object.put("streamaddress", getStreamLink());
    object.put("inuse", isInUse());
    return object;
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
  public Preset createPreset(Set<String> tagList, String name) throws CameraConnectionException {
    int cameraId = getId();
    SimplePreset preset = new SimplePreset(cameraId, tagList);
    preset.setName(name);
    return preset;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + super.hashCode();
    result = prime * result + ((streamLink == null) ? 0 : streamLink.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SimpleCamera) {
      SimpleCamera that = (SimpleCamera) obj;
      if (super.equals(that)
          && (this.getStreamLink() != null && this.getStreamLink().equals(that.getStreamLink())
              || this.getStreamLink() == null && that.getStreamLink() == null)
          && (this.mACAddress != null && this.mACAddress .equals(that.mACAddress )
              || this.mACAddress  == null && that.mACAddress  == null)) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public String getMacAddress() throws CameraConnectionException {
    return mACAddress;
  }

  public void setMacAddress(String mACAddress) {
    this.mACAddress = mACAddress;
  }
}
