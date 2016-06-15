package com.benine.backend.http.presethandlers;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.http.RequestHandler;
import com.benine.backend.http.RequestHandlerTest;
import com.benine.backend.http.presethandlers.PresetDeletionHandler;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;


public class PresetDeletionHandlerTest extends RequestHandlerTest {

  private IPCamera ipcamera;
  private SimpleCamera simpleCamera;
  private Preset preset;
  private Stream stream;
  private MJPEGStreamReader streamReader;
  private Set<String> tags;

  @Override
  public RequestHandler supplyHandler() {
    return new PresetDeletionHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, CameraBusyException, JSONException {
    super.initialize();
    ipcamera = mock(IPCamera.class);
    simpleCamera = mock(SimpleCamera.class);
    stream = mock(Stream.class);
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg")));

    streamReader = new MJPEGStreamReader(stream);
    tags = new HashSet<>(Arrays.asList("violin", "piano"));

    try {
      when(streamController.getStreamReader(1)).thenReturn(streamReader);
      when(streamController.getStreamReader(2)).thenReturn(streamReader);
      when(ipcamera.getFocusPosition()).thenReturn(40);
      when(ipcamera.getIrisPosition()).thenReturn(50);
      when(ipcamera.getPosition()).thenReturn(new ZoomPosition(0, 0, 0));
      when(ipcamera.isAutoFocusOn()).thenReturn(true);
      when(ipcamera.isAutoIrisOn()).thenReturn(true);
      when(ipcamera.getId()).thenReturn(1);
      when(simpleCamera.getId()).thenReturn(2);

      preset = new IPCameraPreset(new ZoomPosition(0, 0, 100), 40,50,true, true, 0, "name");
      preset.addTags(tags);

    } catch (CameraConnectionException | StreamNotAvailableException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPresetID() throws Exception{
    setPath("/presets/deletepreset");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("id", "0");
    setParameters(parameters);

    when(presetController.getPresetById(0)).thenReturn(preset);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(requestMock).setHandled(true);
  }

  @Test
  public void testPresetIDPresetNonExistent() throws Exception{
    setPath("/presets/deletepreset");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("id", "0");
    setParameters(parameters);

    when(presetController.getPresetById(0)).thenReturn(null);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(requestMock).setHandled(true);
  }

  @Test
  public void testPresetIDNull() throws Exception{
    setPath("/presets/deletepreset");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(requestMock).setHandled(true);
  }
}
