package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Test the setting in Use handler.
 */
public class SetCameraInUseHandlerTest  extends CameraRequestHandlerTest {

  Camera cam = mock(Camera.class);

  @Before
  public void initialize() throws IOException, CameraBusyException {
    super.initialize();
    when(cameraController.getCameraById(42)).thenReturn(cam);
    when(cameraController.getCameraById(43)).thenReturn(null);
  }

  @Test
  public void testInuseTrue() throws IOException, ServletException {
    setPath("/42/inuse?inuse=true");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("inuse", "true");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);


    verify(out).write("{\"succes\":\"true\"}");
    verify(cam).setInUse();
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testSetNotInUse() throws IOException, ServletException {
    setPath("/42/inuse?inuse=false");

    when(cam.isInUse()).thenReturn(true);

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("inuse", "false");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    when(cam.isInUse()).thenReturn(false);
    verify(out).write("{\"succes\":\"true\"}");
    verify(cam).setNotInUse();
    verify(requestMock).setHandled(true);
  }
  
  @Test
  public void testInuseNoParam() throws IOException, ServletException {
    setPath("/42/inuse");

    MultiMap<String> parameters = new MultiMap<>();
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"succes\":\"false\"}");
    verify(cam, never()).setInUse();
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testCameraNull() throws IOException, ServletException {
    setPath("/43/inuse?inuse=true");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("inuse", "true");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);


    verify(out).write("{\"succes\":\"false\"}");
    verify(cam, never()).setInUse();
    verify(requestMock).setHandled(true);
  }

  @Override
  public CameraRequestHandler supplyHandler() {
    return new SetCameraInUseHandler(httpserver);
  }
}
