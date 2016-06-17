package com.benine.backend.http.camerahandlers;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.http.camerahandlers.CameraStreamHandler;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.Stream;
import com.benine.backend.video.StreamNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Created on 4-5-16.
 */
public class CameraStreamHandlerTest extends CameraRequestHandlerTest {

  IPCamera cam = mock(IPCamera.class);

  Stream stream;
  MJPEGStreamReader streamReader;
  Thread streamReaderThread;

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraStreamHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, CameraBusyException {
    super.initialize();
    when(cameraController.getCameraById(42)).thenReturn(cam);

    stream = mock(Stream.class);
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg")));

    streamReader = new MJPEGStreamReader(stream);
    //when(streamReader.getBoundary()).thenReturn("--testBoundary");

    doThrow(new IOException()).when(httpresponseMock).getOutputStream();
    try {
      when(streamController.getStreamReader(42)).thenReturn(streamReader);
      streamReaderThread = new Thread(streamReader);
      streamReaderThread.start();
    } catch (StreamNotAvailableException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMJPEGContentType() throws Exception {
    setPath("/42/mjpeg");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock).setContentType("multipart/x-mixed-replace;boundary=" + streamReader.getBoundary());
  }

  @Test
  public void testMJPEGCacheControl() throws Exception {
    setPath("/42/mjpeg");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock).setHeader("Cache-Control", "no-store, "
            + "no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0");
  }

  @Test
  public void testMJPEGConnection() throws Exception {
    setPath("/42/mjpeg");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock).setHeader("Connection", "close");
  }

  @Test
  public void testMJPEGPragma() throws Exception {
    setPath("/42/mjpeg");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock).setHeader("Pragma", "no-cache");
  }

  @Test
  public void testMJPEGExpires() throws Exception {
    setPath("/42/mjpeg");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock).setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
  }

}
