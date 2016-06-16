package com.benine.backend.http;

import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.AutoPresetCreator;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.preset.autopresetcreation.SubView;
import org.eclipse.jetty.util.MultiMap;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    verify(out).write("{\"succes\":\"false\"}");
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

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }


  @Override
  public AutoPresetHandler supplyHandler() {
    return new AutoPresetCreationStatusHandler(httpserver);
  }
}
