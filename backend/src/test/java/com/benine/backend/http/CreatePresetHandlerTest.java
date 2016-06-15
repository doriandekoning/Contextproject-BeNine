package com.benine.backend.http;

import com.benine.backend.camera.*;
import com.benine.backend.camera.ipcameracontrol.FocusValue;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.camera.ipcameracontrol.IrisValue;
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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;


public class CreatePresetHandlerTest extends RequestHandlerTest {

  private IPCamera ipcamera;
  private SimpleCamera simpleCamera;
  private Preset preset;
  private Stream stream;
  private MJPEGStreamReader streamReader;
  private Set<String> tags;

  @Override
  public RequestHandler supplyHandler() {
    return new CreatePresetHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, JSONException, CameraBusyException {
    super.initialize();
    ipcamera = mock(IPCamera.class);
    simpleCamera = mock(SimpleCamera.class);
    when(cameraController.getCameraById(1)).thenReturn(ipcamera);
    when(cameraController.getCameraById(2)).thenReturn(simpleCamera);
    stream = mock(Stream.class);
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg")));

    streamReader = new MJPEGStreamReader(stream);
    tags = new HashSet<>(Arrays.asList("violin", "piano"));
    

    try {
      when(streamController.getStreamReader(1)).thenReturn(streamReader);
      when(streamController.getStreamReader(2)).thenReturn(streamReader);
      when(ipcamera.getFocusPosition()).thenReturn(33);
      when(ipcamera.getIrisPosition()).thenReturn(50);
      when(ipcamera.getPosition()).thenReturn(new Position(0, 0));
      when(ipcamera.getZoom()).thenReturn(100);
      when(ipcamera.isAutoFocusOn()).thenReturn(true);
      when(ipcamera.isAutoIrisOn()).thenReturn(true);
      when(ipcamera.getId()).thenReturn(1);
      when(simpleCamera.getId()).thenReturn(2);

      preset = new IPCameraPreset(new ZoomPosition(0,0, 100), new FocusValue(33, true), new IrisValue(50, true), 0);
      preset.setName("name");
      preset.addTags(tags);
      when(ipcamera.createPreset(tags, "test")).thenReturn((IPCameraPreset) preset);

    } catch (CameraConnectionException e) {
      e.printStackTrace();
    } catch (StreamNotAvailableException e) {
      e.printStackTrace();
    } catch (CameraBusyException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCameraID() throws Exception{
    setPath("/presets/createpreset");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("name", "test");
    setParameters(parameters);
    when(presetController.getPresetById(0)).thenReturn(preset);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testCameraIDTags() throws Exception{
    setPath("/presets/createpreset");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("name", "test");
    parameters.add("tags", "violin,piano");
    setParameters(parameters);
    when(presetController.getPresetById(0)).thenReturn(preset);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(presetController).addPreset(preset);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testSimpleCameraID() throws Exception{
    setPath("/presets/createpreset?camera=2");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "2");
    parameters.add("name", "test");
    setParameters(parameters);
    when(presetController.getPresetById(0)).thenReturn(preset);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(requestMock).setHandled(true);
  }

  @Test
  public void testInvalidID() throws Exception{
    setPath("/presets/createpreset?camera=2");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "3");
    parameters.add("name", "test");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testNoName() throws Exception{
    setPath("/presets/createpreset");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "3");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testNoCamera() throws Exception{
    setPath("/presets/createpreset");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testDatabaseException() throws Exception{
    setPath("/presets/createpreset");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("name", "test");
    setParameters(parameters);
    Exception exception = new SQLException();
    when(presetController.addPreset(any())).thenThrow(exception);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testCameraConnectionException() throws Exception{
    setPath("/presets/createpreset");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("name", "test");
    setParameters(parameters);
    Exception exception = new CameraConnectionException("camera can not connect", 1);
    when(ipcamera.createPreset(any(), any())).thenThrow(exception);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testCameraBusyException() throws Exception{
    setPath("/presets/createpreset");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("name", "test");
    setParameters(parameters);
    Exception exception = new CameraBusyException("camera can not connect", 1);
    when(ipcamera.createPreset(any(), any())).thenThrow(exception);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
}
