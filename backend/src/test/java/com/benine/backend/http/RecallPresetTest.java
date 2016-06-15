package com.benine.backend.http;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.Stream;
import com.benine.backend.video.StreamNotAvailableException;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;

import static org.mockito.Mockito.*;

public class RecallPresetTest extends RequestHandlerTest {

  private IPCamera ipcamera;
  private Preset preset;
  private Stream stream;
  private MJPEGStreamReader streamReader;

  @Override
  public RequestHandler supplyHandler() {
    return new RecallPresetHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, CameraBusyException {
    super.initialize();
    ipcamera = mock(IPCamera.class);
    when(cameraController.getCameraById(1)).thenReturn(ipcamera);
    stream = mock(Stream.class);
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg")));

    streamReader = new MJPEGStreamReader(stream);

    try {
      when(streamController.getStreamReader(1)).thenReturn(streamReader);
      when(ipcamera.getFocusPosition()).thenReturn(33);
      when(ipcamera.getIrisPosition()).thenReturn(50);
      when(ipcamera.getPosition()).thenReturn(new Position(0, 0));
      when(ipcamera.getZoom()).thenReturn(100);
      when(ipcamera.isAutoFocusOn()).thenReturn(true);
      when(ipcamera.isAutoIrisOn()).thenReturn(true);
      when(ipcamera.getId()).thenReturn(1);

      preset = mock(IPCameraPreset.class);
      when(preset.getCameraId()).thenReturn(1);
      when(presetController.getPresetById(1)).thenReturn(preset);
    } catch (CameraConnectionException | CameraBusyException | StreamNotAvailableException e) {
      e.printStackTrace();
    }
  }

  public void recall() {
    setPath("/presets/recallpreset?presetid=1");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("presetid", "1");
    setParameters(parameters);
  }

  @Test
  public void testRecallPresetInvalid() throws Exception {
    setPath("/presets/recallpreset?presetid=on");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("presetid", "on");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testHandledSuccess() throws Exception {
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"true\"}");
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRecallPreset() throws IOException, ServletException, CameraConnectionException, CameraBusyException {
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(preset).excecutePreset(ipcamera);
    verify(out).write("{\"succes\":\"true\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testRecallCameraConnectionException() throws Exception {
    doThrow(new CameraConnectionException("test", 0)).when(preset).excecutePreset(ipcamera);
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testRecallCameraBussyException() throws Exception {
    doThrow(new CameraBusyException("test", 0)).when(preset).excecutePreset(ipcamera);
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }

}