package com.benine.backend.video;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines a class controlling all streams.
 */
public class StreamController {

  /**
   * Contains all StreamReader objects.
   */
  private Map<Integer, StreamReader> streams;
  /**
   * Contains the threads on which the streams are running.
   */
  private Map<Integer, Thread> threads;

  /**
   * Constructor for a StreamController.
   */
  public StreamController() {
    this.streams = new HashMap<>();
    this.threads = new HashMap<>();
  }

  /**
   * Adds a camera to the HashMap containing all streams.
   * @param cam a Camera object.
   */
  public void addCamera(Camera cam) {
    StreamReader stream = createStream(cam);

    if (stream != null) {
      Thread thread = new Thread(stream);

      streams.put(cam.getId(), stream);
      threads.put(cam.getId(), thread);

      thread.start();
    }
  }

  /**
   * Creates a stream from a camera.
   * @param cam a Camera object.
   * @return a StreamReader if a stream is available, null otherwise.
   */
  private StreamReader createStream(Camera cam) {
    String streamLink = getStreamLink(cam);
    StreamType type = verifyType(cam);

    switch (type) {
      case MJPEG: return new MJPEGStreamReader(new Stream(streamLink));
      default: return null;
    }

  }

  /**
   * Gets the streamlink of a camera, if the camera has the correct type.
   * @param cam   a Camera object.
   * @return      Streamlink if correct type, empty string else.
   */
  private String getStreamLink(Camera cam) {
    String streamLink = "";

    if (cam instanceof IPCamera) {
      streamLink = ((IPCamera) cam).getStreamLink();
    } else if (cam instanceof SimpleCamera) {
      streamLink = ((SimpleCamera) cam).getStreamLink();
    }

    return streamLink;
  }

  /**
   * Gets the type and sets an ENUM if it matches.
   * @param   cam The camera from which the stream is fetched.
   * @return  A StreamType Enum.
   */
  private StreamType verifyType(Camera cam) {
    if (cam instanceof IPCamera) {
      return StreamType.MJPEG;
    } else if (cam instanceof SimpleCamera) {
      return StreamType.MJPEG;
    } else {
      return StreamType.UNKNOWN;
    }
  }

  /**
   * Returns the streamreader belonging to a camera.
   * @param camId The identifier of the camera.
   * @return  A StreamReader object.
   * @throws StreamNotAvailableException if there is no stream available for a given camera.
   */
  public StreamReader getStreamReader(int camId) throws StreamNotAvailableException {
    if (streams.containsKey(camId)) {
      return streams.get(camId);
    } else {
      throw new StreamNotAvailableException();
    }
  }
}
