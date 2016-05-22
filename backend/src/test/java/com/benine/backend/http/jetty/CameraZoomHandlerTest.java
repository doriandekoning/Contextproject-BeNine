package com.benine.backend.http.jetty;

import com.benine.backend.camera.ZoomingCamera;
import com.benine.backend.http.CameraRequestHandler;
import com.benine.backend.http.CameraZoomHandler;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Created on 4-5-16.
 */
public class CameraZoomHandlerTest extends CameraRequestHandlerTest {

  ZoomingCamera cam = mock(ZoomingCamera.class);

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraZoomHandler();
  }

  @Before
  public void initialize() throws IOException {
    super.initialize();
    when(cameracontroller.getCameraById(42)).thenReturn(cam);
  }

  @Test
  public void testZoomAbsolute() throws Exception {
    setPath("/camera/42/zoom?zoomType=absolute&zoom=2");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("zoomType", "absolute");
    parameters.add("zoom", "2");

    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).zoomTo(2);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testZoomRelative() throws Exception {
    setPath("/camera/42/zoom?zoomType=relative&zoom=2");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("zoomType", "relative");
    parameters.add("zoom", "2");

    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(cam).zoom(2);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testZoomMalformedURI() throws Exception {
    setPath("/camera/42/zoom?zoomType=relative&soom=2");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("zoomType", "relative");
    parameters.add("soom", "2");

    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    String response = "{\"succes\":\"false\"}";
    verify(out).write(response);
    verify(requestMock).setHandled(true);
  }


}
