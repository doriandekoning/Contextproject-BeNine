package com.benine.backend.http;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.IrisCamera;
import org.eclipse.jetty.util.MultiMap;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created on 4-5-16.
 */
public class CameraIrisHandlerTest extends CameraRequestHandlerTest {

  IrisCamera cam = mock(IrisCamera.class);

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraIrisHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, JSONException, CameraBusyException {
    super.initialize();
    when(cameraController.getCameraById(42)).thenReturn(cam);
  }

  @Test
  public void testOnlyAutoIris() throws Exception {
    setPath("/42/iris?autoIrisOn=true");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("autoIrisOn", "true");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).setAutoIrisOn(true);
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testOnlyIrisPosIris() throws Exception {
    setPath("/42/iris?position=3");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "3");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).setIrisPosition(3);
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testAutoIrisOnAndIrisPosIris() throws Exception {
    setPath("/42/iris?position=3&autoIrisOn=true");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "3");
    parameters.add("autoIrisOn", "true");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).setIrisPosition(3);
    verify(cam).setAutoIrisOn(true);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testMalformedURI() throws Exception {
    setPath("/42/iris?position=3&position=true");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "3");
    parameters.add("position", "true");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
  }
  
  @Test
  public void testCameraConnectionException() throws Exception {
    setPath("/42/iris?position=3");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("position", "3");
    setParameters(parameters);

    doThrow(new CameraConnectionException("test exception", 0)).when(cam).setIrisPosition(eq(3));
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
  }

  @Test
  public void testMoveIrisRelative() throws Exception {
    setPath("/42/iris?speed=5");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("speed", "5");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).moveIris(5);
    verify(requestMock).setHandled(true);
  }
}
