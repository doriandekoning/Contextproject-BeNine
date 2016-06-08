package com.benine.backend.http.presetqueueHandler;

import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.http.presetqueue.CreatePresetQueueHandler;
import com.benine.backend.http.presetqueue.PresetQueueRequestHandler;
import com.benine.backend.performance.PresetQueue;

public class CreatePresetQueueHandlerTest extends PresetQueueRequestHandlerTest {

  @Override
  public PresetQueueRequestHandler supplyHandler() {
    return new CreatePresetQueueHandler(httpserver);
  }
  
  @Before
  public void initialize() throws IOException {
    super.initialize();
  }
  
  @Test
  public void testCreatePresetQueue() throws Exception{
    setPath("/presetqueues/create");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("name", "testQueue");
    setParameters(parameters);
    
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    PresetQueue expected = new PresetQueue("testQueue", new ArrayList<>());
    verify(presetQueueController).addPresetQueue(expected);
    verify(requestMock).setHandled(true);
  }
  

}
