package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jetty.util.MultiMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.Stream;
import com.benine.backend.video.StreamNotAvailableException;


public class EditPresetHandlerTest extends RequestHandlerTest {

  private IPCamera ipcamera= mock(IPCamera.class);
  private IPCameraPreset preset;
  private Stream stream;
  private MJPEGStreamReader streamReader;
  private Set<String> tags;
  
  @Override
   public RequestHandler supplyHandler() {
     return new EditPresetHandler();
   }
 
   
  @Before
  public void initialize() throws IOException {
    super.initialize();
    
    when(cameracontroller.getCameraById(1)).thenReturn(ipcamera);
    stream = mock(Stream.class);
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg")));

    streamReader = new MJPEGStreamReader(stream);
    tags = new HashSet<>(Arrays.asList("violin", "piano"));

    preset = new IPCameraPreset(new Position(0,0), 100, 33,50,true,15,1,true, 1, tags);
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
    parameters.add("presetid", "1");
    parameters.add("tags", "test");
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(requestMock).setHandled(true);
    Set<String> tags = new HashSet<String>();
    tags.add("test");
    Assert.assertEquals(tags, preset.getTags());
  }
  
  @Test
  public void testUpdatePosition() throws Exception{
    when(ipcamera.getZoomPosition()).thenReturn(50);
    
    setPath("/presets/edit");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    parameters.add("overwritetag", "false");
    parameters.add("overwriteposition", "true");
    parameters.add("presetid", "1");
    parameters.add("tags", "test");
    setParameters(parameters);
    
    IPCameraPreset preset2 = new IPCameraPreset(new Position(0,0), 50, 33,50,true,15,1,true, 1, tags);
    when(ipcamera.createPreset(tags)).thenReturn(preset2);
        
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    
    verify(requestMock).setHandled(true);
    verify(presetController).updatePreset(preset2);
  }
}
