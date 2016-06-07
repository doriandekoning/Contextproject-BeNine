package com.benine.backend.http;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.Stream;
import com.benine.backend.video.StreamNotAvailableException;
import org.eclipse.jetty.util.MultiMap;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
  public void initialize() throws IOException, JSONException {
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

      preset = new IPCameraPreset(new ZoomPosition(0,0, 100), 33,50,true,true, 0);
      preset.setCameraId(1);
      when(presetController.getPresetById(1)).thenReturn(preset);
    } catch (CameraConnectionException | StreamNotAvailableException e) {
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
  public void testRecallPresetMove() throws Exception {
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(ipcamera).moveTo(new Position(0, 0), 15, 1);
  }

  @Test
  public void testRecallPresetZoom() throws Exception {
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(ipcamera).zoomTo(100);
  }

  @Test
  public void testRecallPresetMoveFocus() throws Exception {
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(ipcamera).moveFocus(33);
  }

  @Test
  public void testRecallPresetAutoFocus() throws Exception {
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(ipcamera).setAutoFocusOn(true);
  }

  @Test
  public void testRecallPresetAutoIris() throws Exception {
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(ipcamera).setAutoIrisOn(true);
  }

  @Test
  public void testRecallPresetIrisPosition() throws Exception {
    recall();
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(ipcamera).setIrisPosition(50);
  }

}