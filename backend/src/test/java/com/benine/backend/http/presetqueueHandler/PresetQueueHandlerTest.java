package com.benine.backend.http.presetqueueHandler;

import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.http.presetqueue.PresetQueueHandler;
import com.benine.backend.http.presetqueue.PresetQueueRequestHandler;


public class PresetQueueHandlerTest extends PresetQueueRequestHandlerTest {

  @Override
  public PresetQueueRequestHandler supplyHandler() {
    return new PresetQueueHandler(httpserver);
  }
  
  @Before
  public void initialize() throws IOException {
    super.initialize();
    
  }
  @Test
  public void testHandleInvalidPresetQueueID() throws IOException, ServletException {
    setPath("/invalid/mjpeg");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(requestMock).setHandled(true);
  }

}
