package com.benine.backend.http.presetqueueHandler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.http.presetqueue.DeletePresetQueueHandler;
import com.benine.backend.http.presetqueue.PresetQueueRequestHandler;
import com.benine.backend.performance.PresetQueue;

public class DeletePresetQueueHandlerTest extends PresetQueueRequestHandlerTest {
  
  PresetQueue presetQueue = mock(PresetQueue.class);

  @Override
  public PresetQueueRequestHandler supplyHandler() {
    return new DeletePresetQueueHandler(httpserver);
  }
  
  @Before
  public void initialize() throws IOException {
    super.initialize();
    when(presetQueueController.getPresetQueueById(1)).thenReturn(presetQueue);
  }
  
  @Test
  public void testDeletePresetQueue() throws Exception{
    setPath("/1/delete");
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(presetQueueController).removePresetQueue(presetQueue);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testDeletePresetQueueNonExcisting() throws Exception{
    setPath("/5/delete");
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  

}
