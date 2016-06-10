package com.benine.backend.http;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the auto preset creation handler.
 */
public class AutoPresetCreationHandlerTest extends AutoPresetHandlerTest {
  @Override
  public AutoPresetHandler supplyHandler() {
    return new AutoPresetCreationHandler(httpserver);
  }

  @Test
  public void testNonIPCam() throws Exception{
    SimpleCamera cam = Mockito.mock(SimpleCamera.class);
    when(cameraController.getCameraById(3204)).thenReturn(cam);
    setPath("/presets/autocreatepresets");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.put("camera", "3204");
    setParameters(parameters);


    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testNoConnection() throws Exception{
    IPCamera cam = Mockito.mock(IPCamera.class);
    when(cam.getPosition()).thenThrow(CameraConnectionException.class);
    when(cameraController.getCameraById(3204)).thenReturn(cam);
    setPath("/presets/autocreatepresets");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.put("camera", "3204");
    setParameters(parameters);


    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testHandleBusy() throws Exception{
    IPCamera cam = Mockito.mock(IPCamera.class);
    when(cam.getPosition()).thenThrow(CameraBusyException.class);
    when(cameraController.getCameraById(3204)).thenReturn(cam);
    setPath("/presets/autocreatepresets");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.put("camera", "3204");
    setParameters(parameters);


    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(requestMock).setHandled(true);
  }
}
