package com.benine.backend.video;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.util.HashMap;
import java.util.Map;

public class StreamController {

  private Map<Integer, StreamReader> streams;
  private Map<Integer, Thread> threads;

  public StreamController() {
    this.streams = new HashMap<>();
    this.threads = new HashMap<>();
  }

  public void addCamera(Camera cam) {
    StreamReader stream = createStream(cam);

    if (stream != null) {
      Thread thread = new Thread(stream);

      streams.put(cam.getId(), stream);
      threads.put(cam.getId(), thread);

      thread.start();
    }
  }

  private StreamReader createStream(Camera cam) {
    String streamLink = getStreamLink(cam);
    StreamType type = verifyType(streamLink);

    switch(type) {
      case MJPEG: return new MJPEGStreamReader(new Stream(streamLink));
      default: return null;
    }

  }

  private String getStreamLink(Camera cam) {
    String streamLink = "";

    if (cam instanceof IPCamera) {
      streamLink = ((IPCamera) cam).getStreamLink();
    } else if (cam instanceof SimpleCamera) {
      streamLink = ((SimpleCamera) cam).getStreamLink();
    }

    return streamLink;
  }

  private StreamType verifyType(String link) {

    if (link.endsWith(".mjpeg")) {
      return StreamType.MJPEG;
    } else {
      return StreamType.UNKNOWN;
    }
  }

  public StreamReader getStreamReader(int camId) throws StreamNotAvailableException {
    if (streams.containsKey(camId)) {
      return streams.get(camId);
    } else {
      throw new StreamNotAvailableException();
    }
  }
}
