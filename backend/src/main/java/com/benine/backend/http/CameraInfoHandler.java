package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CameraInfoHandler extends CameraRequestHandler {

  /**
   * Map containing the handlers, <route, handler>;
   */
  private Map<String, CameraRequestHandler> handlers;

  /**
   * Constructor for a new CameraInfoHandler, handling the /camera/ request.
   */
  public CameraInfoHandler() {
    this.handlers = new HashMap<>();

    setHandlers(new CameraStreamHandler(), new CameraFocusHandler(),
            new CameraIrisHandler(), new CameraMovingHandler(),
            new CameraZoomHandler());
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    String cameraInfo = getCameraController().getCamerasJSON();

    boolean routed = false;
    String route = getRoute(request);

    if (checkValidCameraID(request) && handlers.containsKey(route)) {
      Camera cam = getCameraController().getCameraById(getCameraId(request));
      CameraRequestHandler handler = handlers.get(route);

      if (handler.isAllowed(cam)) {
        handler.handle(s, request, req, res);
        routed = true;
      }

    }

    if (!routed) {
      respond(request, res, cameraInfo);
      request.setHandled(true);
    }
  }

  /**
   * Checks if the camera id of this request is valid.
   * @param request   The current request.
   * @return          True if valid, False if invalid.
   */
  private boolean checkValidCameraID(Request request) {
    int id = getCameraId(request);
    Camera camera = getCameraController().getCameraById(id);

    return id > 0 && camera != null;
  }

  /**
   * Sets the handlers of this cameraInfoHandler
   * @param streamHandler A StreamHandler object.
   * @param focusHandler A FocusHandler object.
   * @param irisHandler An IrisHandler object.
   * @param movingHandler A MovingHandler object.
   * @param zoomHandler A ZoomHandler object.
   */
  public void setHandlers(CameraStreamHandler streamHandler,  CameraFocusHandler focusHandler,
                     CameraIrisHandler irisHandler,
                     CameraMovingHandler movingHandler,
                     CameraZoomHandler zoomHandler) {

    handlers.put("mjpeg", streamHandler);
    handlers.put("focus", focusHandler);
    handlers.put("move", movingHandler);
    handlers.put("iris", irisHandler);
    handlers.put("zoom", zoomHandler);
  }

  @Override
  boolean isAllowed(Camera cam) {
    return cam != null;
  }
}
