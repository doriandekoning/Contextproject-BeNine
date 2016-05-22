package com.benine.backend.http.jetty;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.benine.backend.http.CameraMovingHandler;
import com.benine.backend.http.CameraRequestHandler;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created on 4-5-16.
 */
public class CameraMovingHandlerTest extends CameraRequestHandlerTest{

  MovingCamera cam = mock(MovingCamera.class);

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraMovingHandler();
  }

  @Before
  public void initialize() throws IOException {
    super.initialize();
    when(cameracontroller.getCameraById(42)).thenReturn(cam);
  }

  @Test
  public void testMoveAbsolute() throws Exception {
    setPath("/camera/42/move?pan=1&tilt=2&moveType=absolute&panSpeed=3&tiltSpeed=4");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("pan", "1");
    parameters.add("tilt", "2");
    parameters.add("moveType", "absolute");
    parameters.add("panSpeed", "3");
    parameters.add("tiltSpeed", "4");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).moveTo(any(Position.class), eq(3), eq(4));
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testMalformedURI() throws Exception {
    setPath("/camera/42/move?pan=1&pan=2&tilt=2&moveType=absolute&panSpeed=3&tiltSpeed=4");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("pan", "1");
    parameters.add("pan", "2");
    parameters.add("tilt", "2");
    parameters.add("moveType", "absolute");
    parameters.add("panSpeed", "3");
    parameters.add("tiltSpeed", "4");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testMoveWithNotAllValues() throws Exception {
    setPath("/camera/42/move?pan=5");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("pan", "5");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testCameraConnectionException() throws Exception {
    setPath("/camera/42/move?pan=1&tilt=2&moveType=absolute&panSpeed=3&tiltSpeed=4");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("pan", "1");
    parameters.add("tilt", "2");
    parameters.add("moveType", "absolute");
    parameters.add("panSpeed", "3");
    parameters.add("tiltSpeed", "4");
    setParameters(parameters);

    setParameters(parameters);
    doThrow(new CameraConnectionException("test exception", 0)).when(cam).moveTo(any(Position.class), eq(3), eq(4));

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }
}