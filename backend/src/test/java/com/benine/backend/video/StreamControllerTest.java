package com.benine.backend.video;

import com.benine.backend.camera.Camera;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

/**
 * Created by Jochem on 18-05-16.
 */
public class StreamControllerTest {

  private StreamController controller;
  private HashMap<Integer, Thread> threads;
  private HashMap<Integer, StreamReader> streams;

  @Before
  public void init() {
    threads = Mockito.mock(HashMap.class);
    streams = Mockito.mock(HashMap.class);

    controller = new StreamController(streams, threads);
  }

  @Test
  public void testAddCameraWithoutStreamThread() {
    Camera cam = Mockito.mock(Camera.class);

    controller.addCamera(cam);

    Mockito.verifyZeroInteractions(threads);
  }

  @Test
  public void testAddCameraWithoutStreamStream() {
    Camera cam = Mockito.mock(Camera.class);

    controller.addCamera(cam);

    Mockito.verifyZeroInteractions(streams);
  }

  @Test (expected = StreamNotAvailableException.class)
  public void testGetSnapshotNotAvailable() throws StreamNotAvailableException {
    controller.getStreamReader(42);
  }
}
