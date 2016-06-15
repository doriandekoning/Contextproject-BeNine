package com.benine.backend.http.presethandlers;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.http.RequestHandler;
import com.benine.backend.http.RequestHandlerTest;
import com.benine.backend.http.presethandlers.EditPresetHandler;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.Stream;
import com.benine.backend.video.StreamNotAvailableException;
import org.eclipse.jetty.util.MultiMap;
import org.json.JSONException;
import org.junit.Assert;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class EditPresetHandlerTest extends RequestHandlerTest {

  private IPCamera ipcamera= mock(IPCamera.class);
  private IPCameraPreset preset;
  private Stream stream;
  private MJPEGStreamReader streamReader;
  private Set<String> tags;
  
  @Override
   public RequestHandler supplyHandler() {
     return new EditPresetHandler(httpserver);
   }
 
   
  @Before
  public void initialize() throws IOException, JSONException, CameraBusyException {
    super.initialize();
    
    when(cameraController.getCameraById(1)).thenReturn(ipcamera);
    stream = mock(Stream.class);
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg")));

    streamReader = new MJPEGStreamReader(stream);
    tags = new HashSet<>(Arrays.asList("violin", "piano"));

    preset = new IPCameraPreset(new ZoomPosition(0,0, 100), 33,50,true,true, 1, "name");
    preset.addTags(tags);
    preset.setId(1);

    when(presetController.getPresetById(1)).thenReturn(preset);
    try {
      when(streamController.getStreamReader(1)).thenReturn(streamReader);
    } catch (StreamNotAvailableException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testUpdateTags() throws Exception{
    setPath("/presets/edit");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("overwritetag", "true");
    parameters.add("overwriteposition", "false");
    parameters.add("overwritename", "false");
    parameters.add("presetid", "1");
    parameters.add("tags", "test");
    parameters.add("name", "name");
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(requestMock).setHandled(true);
    Set<String> tags = new HashSet<String>();
    tags.add("test");
    Assert.assertEquals(tags, preset.getTags());
  }
  
  @Test
  public void testUpdatePosition() throws Exception{
    when(ipcamera.getZoom()).thenReturn(50);
    
    setPath("/presets/edit");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("overwritetag", "false");
    parameters.add("overwriteposition", "true");
    parameters.add("overwritename", "false");
    parameters.add("presetid", "1");
    parameters.add("tags", "test");
    parameters.add("name", "name");
    setParameters(parameters);

    IPCameraPreset preset2 = new IPCameraPreset(new ZoomPosition(0,0, 50), 33,50,true,true, 1, "name");
    preset2.addTags(tags);
    when(ipcamera.createPreset(tags, "name")).thenReturn(preset2);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    
    verify(requestMock).setHandled(true);
    verify(presetController).updatePreset(preset2);
  }

  @Test
  public void testUpdateName() throws Exception{
    when(ipcamera.getPosition()).thenReturn(new ZoomPosition(20, 20, 50));

    setPath("/presets/edit");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("overwritetag", "false");
    parameters.add("overwriteposition", "false");
    parameters.add("overwritename", "true");
    parameters.add("presetid", "1");
    parameters.add("tags", "test");
    parameters.add("name", "name2");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(requestMock).setHandled(true);
    Assert.assertEquals("name2", preset.getName());
  }
  
  @Test
  public void testDatabaseException() throws Exception{
    setPath("/presets/edit");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("overwritetag", "false");
    parameters.add("overwriteposition", "false");
    parameters.add("overwritename", "true");
    parameters.add("presetid", "1");
    parameters.add("tags", "test");
    parameters.add("name", "name2");
    setParameters(parameters);
    Exception exception = new SQLException();
    doThrow(exception).when(presetController).updatePreset(any());
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testCameraConnectionException() throws Exception{
    setPath("/presets/edit");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("overwritetag", "false");
    parameters.add("overwriteposition", "true");
    parameters.add("overwritename", "true");
    parameters.add("presetid", "1");
    parameters.add("tags", "test");
    parameters.add("name", "name2");
    setParameters(parameters);
    Exception exception = new CameraConnectionException("camera can not connect", 1);
    when(ipcamera.createPreset(any(), any())).thenThrow(exception);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testCameraBusyException() throws Exception{
    setPath("/presets/edit");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("overwritetag", "false");
    parameters.add("overwriteposition", "true");
    parameters.add("overwritename", "true");
    parameters.add("presetid", "1");
    parameters.add("tags", "test");
    parameters.add("name", "name2");
    setParameters(parameters);
    Exception exception = new CameraBusyException("camera can not connect", 1);
    when(ipcamera.createPreset(any(), any())).thenThrow(exception);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
}
