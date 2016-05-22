package com.benine.backend.http.jetty;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.http.CameraFocusHandler;
import com.benine.backend.http.CameraRequestHandler;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Created on 4-5-16.
 */
public class CameraFocusHandlerTest extends CameraRequestHandlerTest {

  FocussingCamera cam = mock(FocussingCamera.class);

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraFocusHandler();
  }

  @Before
  public void initialize() throws IOException {
    super.initialize();
    when(cameracontroller.getCameraById(42)).thenReturn(cam);
  }

  @Test
  public void testOnlyAutoFocus() throws Exception {
    setPath("/camera/42/focus?autoFocusOn=true");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("autoFocusOn", "true");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).setAutoFocusOn(true);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testMoveFocusRelative() throws Exception {
    setPath("/camera/42/focus?speed=5");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("speed", "5");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).moveFocus(5);
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testOnlyPosition() throws Exception {
    setPath("/camera/42/focus?position=3");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "3");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).setFocusPosition(3);
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testPositionAndAutoFocus() throws Exception {
    setPath("/camera/42/focus?position=3&autoFocusOn=false");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "3");
    parameters.add("autoFocusOn", "false");

    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).setFocusPosition(3);
    verify(cam).setAutoFocusOn(false);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testMalformedURI() throws Exception {
    setPath("/camera/42/focus?position=4&position=3&autoFocusOn=false");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "4");
    parameters.add("position", "3");
    parameters.add("autoFocusOn", "false");

    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testCameraConnectionException() throws Exception {
    setPath("/camera/42/focus?position=4&autoFocusOn=false");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "4");
    parameters.add("autoFocusOn", "false");

    setParameters(parameters);
    doThrow(new CameraConnectionException("Test Exception", 42)).when(cam).setFocusPosition(4);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
}
