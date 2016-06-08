package com.benine.backend.http.presetqueueHandler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.ServletException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.http.presetqueue.CreatePresetQueueHandler;
import com.benine.backend.http.presetqueue.PresetQueueHandler;
import com.benine.backend.http.presetqueue.PresetQueueRequestHandler;
import static org.mockito.Mockito.when;


public class PresetQueueHandlerTest extends PresetQueueRequestHandlerTest {
  
  JSONObject jsonObject;
  CreatePresetQueueHandler createHandler;

  @Override
  public PresetQueueRequestHandler supplyHandler() {
    return new PresetQueueHandler(httpserver);
  }
  
  @Before
  public void initialize() throws IOException {
    super.initialize();
    jsonObject = new JSONObject();
    JSONArray array = new JSONArray();
    jsonObject.put("presetqueues", array);
    when(presetQueueController.getPresetQueueJSON()).thenReturn(jsonObject.toJSONString());
    createHandler = mock(CreatePresetQueueHandler.class);
    ((PresetQueueHandler) getHandler()).addHandler("/create", createHandler);
  }
  
  @Test
  public void testHandlePresetQueues() throws IOException, ServletException {
    setPath("/invalid/mjpeg");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(out).write(jsonObject.toJSONString());
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testRouteCreate() throws IOException, ServletException {
    setPath("/create");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(createHandler).handle(target, requestMock, httprequestMock, httpresponseMock);
  }

}
