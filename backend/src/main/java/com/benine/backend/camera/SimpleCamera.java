package com.benine.backend.camera;

import com.benine.backend.video.StreamType;
import org.json.simple.JSONObject;

/**
 * Created by dorian on 5-5-16.
 */
public class SimpleCamera extends BasicCamera {

  private String streamLink;

  /**
   * Defines a simple camera, which cannot be controlled.
   */
  public SimpleCamera() {
    super(StreamType.MJPEG);
  }

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
          ) {
        return true;
      }
    }
    return false;
  }
}
