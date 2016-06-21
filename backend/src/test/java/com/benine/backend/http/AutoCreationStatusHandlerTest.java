package com.benine.backend.http;

import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 *
 */
public class AutoCreationStatusHandlerTest extends AutoPresetHandlerTest {

  @Test
  public void testHandleWithParams() throws Exception {
    setPath("/presets/autocreatepresetsstatus");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"success\":\"false\"}");
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testCreatorNull() throws Exception {
    setPath("/presets/autocreatepresetsstatus");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("rows", "1");
    parameters.add("columns", "1");
    parameters.add("levels", "1");
    parameters.add("camera", "2345345");
    setParameters(parameters);
    when(cameraController.getCameraById(2345345)).thenReturn(mock(IPCamera.class));
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"success\":\"false\"}");
    verify(requestMock).setHandled(true);
  }


  @Override
  public AutoPresetHandler supplyHandler() {
    return new AutoPresetCreationStatusHandler(httpserver);
  }
}
