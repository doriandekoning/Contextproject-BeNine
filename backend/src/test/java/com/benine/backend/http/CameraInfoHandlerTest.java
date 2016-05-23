package com.benine.backend.http;

import com.benine.backend.ServerController;
import com.benine.backend.camera.BasicCamera;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ZoomingCamera;
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

  private CameraStreamHandler streamHandler = mock(CameraStreamHandler.class);

  private CameraFocusHandler focusHandler = mock(CameraFocusHandler.class);;

  private CameraMovingHandler moveHandler = mock(CameraMovingHandler.class);;

  private CameraIrisHandler irisHandler = mock(CameraIrisHandler.class);;

  private CameraZoomHandler zoomHandler = mock(CameraZoomHandler.class);;

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraInfoHandler();
  }

  @Before
  public void initialize() throws IOException {
    super.initialize();
    caminfo = "testcaminfo";
    cameracontroller = ServerController.getInstance().getCameraController();

    when(cameracontroller.getCamerasJSON()).thenReturn(caminfo);
    when(cameracontroller.getCameraById(42)).thenReturn(new SimpleCamera());
    when(cameracontroller.getCameraById(43)).thenReturn(null);
    when(cameracontroller.getCameraById(44)).thenReturn(mock(BasicCamera.class));

    ((CameraInfoHandler) getHandler()).setHandlers(streamHandler, focusHandler, irisHandler, moveHandler, zoomHandler);
  }

  @Test
  public void testHandleInvalidCamera() throws IOException, ServletException {
    setPath("/camera/43/mjpeg");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }

  @Test
  public void testHandleInvalidCameraID() throws IOException, ServletException {
    setPath("/camera/invalid/mjpeg");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }

  @Test
  public void testValidCameraNoRoute() throws IOException, ServletException {
    setPath("/camera/42");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }

  @Test
  public void testRouteMJPEG() throws IOException, ServletException {
    setPath("/camera/42/mjpeg");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(streamHandler).handle(target, request, httprequest, httpresponse);
  }

  @Test
  public void testRouteMJPEGNotAvailable() throws IOException, ServletException {
    setPath("/camera/44/mjpeg");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }


  @Test
  public void testRouteFocus() throws IOException, ServletException {
    setPath("/camera/42/focus");
    when(cameracontroller.getCameraById(42)).thenReturn(mock(FocussingCamera.class));

    getHandler().handle(target, request, httprequest, httpresponse);
    verify(focusHandler).handle(target, request, httprequest, httpresponse);
  }

  @Test
  public void testRouteFocusNotAvailable() throws IOException, ServletException {
    setPath("/camera/44/focus");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }

  @Test
  public void testRouteIris() throws IOException, ServletException {
    setPath("/camera/42/iris");
    when(cameracontroller.getCameraById(42)).thenReturn(mock(IrisCamera.class));

    getHandler().handle(target, request, httprequest, httpresponse);
    verify(irisHandler).handle(target, request, httprequest, httpresponse);
  }


  @Test
  public void testRouteIrisNotAvailable() throws IOException, ServletException {
    setPath("/camera/44/iris");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }

  @Test
  public void testRouteZoom() throws IOException, ServletException {
    setPath("/camera/42/zoom");
    when(cameracontroller.getCameraById(42)).thenReturn(mock(ZoomingCamera.class));

    getHandler().handle(target, request, httprequest, httpresponse);
    verify(zoomHandler).handle(target, request, httprequest, httpresponse);
  }

  @Test
  public void testRouteZoomNotAvailable() throws IOException, ServletException {
    setPath("/camera/44/zoom");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }

  @Test
  public void testRouteMove() throws IOException, ServletException {
    setPath("/camera/42/move");
    when(cameracontroller.getCameraById(42)).thenReturn(mock(MovingCamera.class));

    getHandler().handle(target, request, httprequest, httpresponse);
    verify(moveHandler).handle(target, request, httprequest, httpresponse);
  }

  @Test
  public void testRouteMoveNotAvailable() throws IOException, ServletException {
    setPath("/camera/44/move");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }

  @Test
  public void testRouteUnknown() throws IOException, ServletException {
    setPath("/camera/44/thisisanunknownroute");
    getHandler().handle(target, request, httprequest, httpresponse);
    verify(httpresponse.getWriter()).write(caminfo);
    verify(request).setHandled(true);
  }

}
