package com.benine.backend.http.presetqueueHandler;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.http.presetqueue.DeletePresetFromPresetQueueHandler;
import com.benine.backend.http.presetqueue.PresetQueueRequestHandler;
import com.benine.backend.performance.PresetQueue;
import com.benine.backend.preset.Preset;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class DeletePresetFromPresetQueueHandlerTest extends PresetQueueRequestHandlerTest {
  
  PresetQueue presetQueue = mock(PresetQueue.class);
  Preset preset = mock(Preset.class);

  @Override
  public PresetQueueRequestHandler supplyHandler() {
    return new DeletePresetFromPresetQueueHandler(httpserver);
  }
  
  @Before
  public void initialize() throws IOException, CameraBusyException {
    super.initialize();
    when(presetQueueController.getPresetQueueById(1)).thenReturn(presetQueue);
    when(presetController.getPresetById(1)).thenReturn(preset);
  }
  
  @Test
  public void testDeletePresetFromPresetQueue() throws Exception{
    setPath("/1/deletepreset");
    
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "2");
    setParameters(parameters);
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(presetQueue).deletePreset(2);
    verify(presetQueueController).updatePresetQueue(presetQueue);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testDeletePresetFromNonExcistingQueue() throws Exception{
    setPath("/5/deletepreset");
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"success\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testDeletePresetFromPresetQueueWithoutPosition() throws Exception{
    setPath("/1/deletepreset");
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"success\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
 

}
