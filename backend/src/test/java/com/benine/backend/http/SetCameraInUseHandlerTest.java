package com.benine.backend.http;//TODO add Javadoc comment

import com.benine.backend.camera.Camera;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 *
 */
public class SetCameraInUseHandlerTest  extends CameraRequestHandlerTest {

  Camera cam = mock(Camera.class);

  @Before
  public void initialize() throws IOException {
    super.initialize();
    when(cameracontroller.getCameraById(42)).thenReturn(cam);
  }

  @Test
  public void testInuseTrue() throws IOException, ServletException {
    setPath("/42/inuse?inuse=true");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("inuse", "true");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).setInUse(true);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testInuseNoParam() throws IOException, ServletException {
    setPath("/42/inuse");

    MultiMap<String> parameters = new MultiMap<>();
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam, never()).setInUse(true);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testInuseAlreadyInuse() throws IOException, ServletException {
    setPath("/42/inuse?inuse=true");

    when(cam.isInUse()).thenReturn(true);

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("inuse", "true");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam, never()).setInUse(true);
    verify(requestMock).setHandled(true);
    //cleanup
    when(cam.isInUse()).thenReturn(false);
  }

  @Override
  public CameraRequestHandler supplyHandler() {
    return new SetCameraInUseHandler();
  }
}
