package com.benine.backend.video;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

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
  public void testAddCameraIpThread() {
    IPCamera cam = Mockito.mock(IPCamera.class);
    Mockito.when(cam.getStreamLink()).thenReturn("http://localhost");

    controller.addCamera(cam);

    Mockito.verify(threads).put(eq(0), any(Thread.class));
  }

  @Test
  public void testAddCameraIpStream() {
    IPCamera cam = Mockito.mock(IPCamera.class);
    Mockito.when(cam.getStreamLink()).thenReturn("http://localhost");

    controller.addCamera(cam);

    Mockito.verify(streams).put(eq(0), any(MJPEGStreamReader.class));
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
