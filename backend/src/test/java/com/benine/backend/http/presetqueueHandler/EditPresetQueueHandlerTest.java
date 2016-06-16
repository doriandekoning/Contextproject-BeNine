package com.benine.backend.http.presetqueueHandler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.benine.backend.camera.CameraBusyException;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.http.presetqueue.EditPresetQueueHandler;
import com.benine.backend.http.presetqueue.PresetQueueRequestHandler;
import com.benine.backend.performance.PresetQueue;

public class EditPresetQueueHandlerTest extends PresetQueueRequestHandlerTest {
  
  PresetQueue presetQueue = mock(PresetQueue.class);

  @Override
  public PresetQueueRequestHandler supplyHandler() {
    return new EditPresetQueueHandler(httpserver);
  }
  
  @Before
  public void initialize() throws IOException, CameraBusyException {
    super.initialize();
    when(presetQueueController.getPresetQueueById(1)).thenReturn(presetQueue);
  }
  
  @Test
  public void testEditPresetQueue() throws Exception{
    setPath("/1/edit");
    
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("name", "test");
    setParameters(parameters);
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(presetQueue).setName("test");
    verify(presetQueueController).updatePresetQueue(presetQueue);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testEditPresetQueueWithoutName() throws Exception{
    setPath("/1/delete");
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testEditPresetQueueNonExcisting() throws Exception{
    setPath("/5/delete");
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  

}
