package com.benine.backend.http.presetqueueHandler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.benine.backend.camera.CameraBusyException;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.http.presetqueue.AddPresetToPresetQueueHandler;
import com.benine.backend.http.presetqueue.PresetQueueRequestHandler;
import com.benine.backend.performance.PresetQueue;
import com.benine.backend.preset.Preset;

public class AddPresetToPresetQueueHandlerTest extends PresetQueueRequestHandlerTest {
  
  PresetQueue presetQueue = mock(PresetQueue.class);
  Preset preset = mock(Preset.class);

  @Override
  public PresetQueueRequestHandler supplyHandler() {
    return new AddPresetToPresetQueueHandler(httpserver);
  }
  
  @Before
  public void initialize() throws IOException, CameraBusyException {
    super.initialize();
    when(presetQueueController.getPresetQueueById(1)).thenReturn(presetQueue);
    when(presetController.getPresetById(1)).thenReturn(preset);
  }
  
  @Test
  public void testAddPresetToPresetQueue() throws Exception{
    setPath("/1/add");
    
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("presetid", "1");
    parameters.add("position", "2");
    setParameters(parameters);
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(presetQueue).insertPreset(2, preset);
    verify(presetQueueController).updatePresetQueue(presetQueue);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testAddPresetToPresetQueueWithouthPlace() throws Exception{
    setPath("/1/add");
    
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("presetid", "1");
    setParameters(parameters);
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(presetQueue).addPresetEnd(preset);
    verify(presetQueueController).updatePresetQueue(presetQueue);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testAddPresetToNonExcistingQueue() throws Exception{
    setPath("/3/add");
    
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("presetid", "1");
    setParameters(parameters);
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testAddPresetToNonExcistingPreset() throws Exception{
    setPath("/1/add");
    
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("presetid", "5");
    setParameters(parameters);
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testAddPresetWithoutPresetID() throws Exception{
    setPath("/1/add");
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }

}
