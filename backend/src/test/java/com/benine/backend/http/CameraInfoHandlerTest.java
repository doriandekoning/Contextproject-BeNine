package com.benine.backend.http;

import com.benine.backend.camera.BasicCamera;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import org.eclipse.jetty.server.Request;
import org.json.JSONException;
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

  private String caminfo;
  private CameraStreamHandler streamHandler;
  private CameraFocusHandler focusHandler;
  private CameraMovingHandler moveHandler;
  private CameraIrisHandler irisHandler;
  private CameraZoomHandler zoomHandler;

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraInfoHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, JSONException {
    super.initialize();
    caminfo = "testcaminfo";

    streamHandler = mock(CameraStreamHandler.class);
    focusHandler = mock(CameraFocusHandler.class);
    moveHandler = mock(CameraMovingHandler.class);
    irisHandler = mock(CameraIrisHandler.class);
    zoomHandler = mock(CameraZoomHandler.class);

    when(cameraController.getCamerasJSON()).thenReturn(caminfo);
    when(cameraController.getCameraById(42)).thenReturn(new SimpleCamera());
    when(cameraController.getCameraById(43)).thenReturn(null);
    when(cameraController.getCameraById(44)).thenReturn(mock(BasicCamera.class));

    ((CameraInfoHandler) getHandler()).addHandler("mjpeg", streamHandler);
    ((CameraInfoHandler) getHandler()).addHandler("focus", focusHandler);
    ((CameraInfoHandler) getHandler()).addHandler("iris", irisHandler);
    ((CameraInfoHandler) getHandler()).addHandler("move", moveHandler);
    ((CameraInfoHandler) getHandler()).addHandler("zoom", zoomHandler);
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
    when(cameraController.getCameraById(44)).thenReturn(ipmock);
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
    when(cameraController.getCameraById(44)).thenReturn(focusMock);
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
    when(cameraController.getCameraById(44)).thenReturn(irisMock);
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
    when(cameraController.getCameraById(44)).thenReturn(zoomingMock);
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
    when(cameraController.getCameraById(44)).thenReturn(movingMock);
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
