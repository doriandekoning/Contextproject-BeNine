package com.benine.backend.video;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.io.IOException;
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
    this(new HashMap<>(), new HashMap<>());
  }

  /**
   * Constructor for a StreamController.
   * @param streams Map for integer, thread
   * @param threads Map for integer, thread
   */
  public StreamController(Map<Integer, StreamReader> streams, Map<Integer, Thread> threads) {
    this.streams = streams;
    this.threads = threads;
  }

  /**
   * Adds a camera to the Map containing all streams if there is a stream available.
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
    StreamType type = cam.getStreamType();

    if (type == null) {
      type = StreamType.UNKNOWN;
    }

    try {
      switch (type) {
        case MJPEG: return new MJPEGStreamReader(new Stream(streamLink));
        default: return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
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
   * Returns the streamreader belonging to a camera.
   * @param camId The identifier of the camera.
   * @return  A StreamReader object.
   * @throws StreamNotAvailableException if there is no stream available for a given camera.
   */
  public StreamReader getStreamReader(int camId) throws StreamNotAvailableException {
    if (streams.containsKey(camId)) {
      return streams.get(camId);
    } else {
      throw new StreamNotAvailableException(camId, "No stream associated with this camera.");
    }
  }
}
