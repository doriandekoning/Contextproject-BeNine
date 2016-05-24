package com.benine.backend.http;

import com.benine.backend.camera.BasicCamera;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.eclipse.jetty.server.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * Created on 22-05-16.
 */
public class CameraInfoHandlerTest extends CameraRequestHandlerTest {

  private String target;

  private Request request;

  private HttpServletRequest httprequest;

  private HttpServletResponse httpresponse;

  private String caminfo;
  private CameraStreamHandler streamHandler;
  private CameraFocusHandler focusHandler;
  private CameraMovingHandler moveHandler;
  private CameraIrisHandler irisHandler;
  private CameraZoomHandler zoomHandler;

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraInfoHandler();
  }

  @Before
  public void initialize() throws IOException {
    super.initialize();
    caminfo = "testcaminfo";

    streamHandler = mock(CameraStreamHandler.class);
    focusHandler = mock(CameraFocusHandler.class);
    moveHandler = mock(CameraMovingHandler.class);
    irisHandler = mock(CameraIrisHandler.class);
    zoomHandler = mock(CameraZoomHandler.class);

    when(cameracontroller.getCamerasJSON()).thenReturn(caminfo);
    when(cameracontroller.getCameraById(42)).thenReturn(new SimpleCamera());
    when(cameracontroller.getCameraById(43)).thenReturn(null);
    when(cameracontroller.getCameraById(44)).thenReturn(mock(BasicCamera.class));

    ((CameraInfoHandler) getHandler()).setHandlers(streamHandler, focusHandler, irisHandler, moveHandler, zoomHandler);
  }

  @Test
  public void testHandleInvalidCamera() throws IOException, ServletException {
    setPath("/43/mjpeg");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testHandleInvalidCameraID() throws IOException, ServletException {
    setPath("/invalid/mjpeg");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testValidCameraNoRoute() throws IOException, ServletException {
    setPath("/42");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteMJPEG() throws IOException, ServletException {
    setPath("/44/mjpeg");

    IPCamera ipmock = mock(IPCamera.class);
    when(cameracontroller.getCameraById(44)).thenReturn(ipmock);
    when(streamHandler.isAllowed(ipmock)).thenReturn(true);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(streamHandler).handle(target, requestMock, httprequestMock, httpresponseMock);
  }

  @Test
  public void testRouteMJPEGNotAvailable() throws IOException, ServletException {
    setPath("/44/mjpeg");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testRouteFocus() throws IOException, ServletException {
    setPath("/44/focus");

    FocussingCamera focusMock = mock(FocussingCamera.class);
    when(cameracontroller.getCameraById(44)).thenReturn(focusMock);
    when(focusHandler.isAllowed(focusMock)).thenReturn(true);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(focusHandler).handle(target, requestMock, httprequestMock, httpresponseMock);
  }

  @Test
  public void testRouteFocusNotAvailable() throws IOException, ServletException {
    setPath("/44/focus");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteIris() throws IOException, ServletException {
    setPath("/44/iris");

    IrisCamera irisMock = mock(IrisCamera.class);
    when(cameracontroller.getCameraById(44)).thenReturn(irisMock);
    when(irisHandler.isAllowed(irisMock)).thenReturn(true);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(irisHandler).handle(target, requestMock, httprequestMock, httpresponseMock);
  }


  @Test
  public void testRouteIrisNotAvailable() throws IOException, ServletException {
    setPath("/44/iris");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteZoom() throws IOException, ServletException {
    setPath("/44/zoom");

    ZoomingCamera zoomingMock = mock(ZoomingCamera.class);
    when(cameracontroller.getCameraById(44)).thenReturn(zoomingMock);
    when(zoomHandler.isAllowed(zoomingMock)).thenReturn(true);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(zoomHandler).handle(target, requestMock, httprequestMock, httpresponseMock);
  }

  @Test
  public void testRouteZoomNotAvailable() throws IOException, ServletException {
    setPath("/44/zoom");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteMove() throws IOException, ServletException {
    setPath("/44/move");

    MovingCamera movingMock = mock(MovingCamera.class);
    when(cameracontroller.getCameraById(44)).thenReturn(movingMock);
    when(moveHandler.isAllowed(movingMock)).thenReturn(true);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(moveHandler).handle(target, requestMock, httprequestMock, httpresponseMock);
  }

  @Test
  public void testRouteMoveNotAvailable() throws IOException, ServletException {
    setPath("/44/move");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteUnknown() throws IOException, ServletException {
    setPath("/44/thisisanunknownroute");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    verify(httpresponseMock.getWriter()).write(caminfo);
    verify(requestMock).setHandled(true);
  }

}
