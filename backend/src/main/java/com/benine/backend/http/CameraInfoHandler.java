package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CameraInfoHandler extends CameraRequestHandler {

  /**
   * Map containing the handlers, (route, handler).
   */
  private Map<String, CameraRequestHandler> handlers;

  /**
   * Constructor for a new CameraInfoHandler, handling the /camera/ request.
   */
  public CameraInfoHandler() {
    this.handlers = new HashMap<>();

    addHandler("mjpeg", new CameraStreamHandler());
    addHandler("focus", new CameraFocusHandler());
    addHandler("move", new CameraMovingHandler());
    addHandler("iris", new CameraIrisHandler());
    addHandler("zoom", new CameraZoomHandler());
  }


  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

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
      String cameraInfo = getCameraController().getCamerasJSON();
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
   * Adds a handler to this cameraInfoHandler.
   * @param uri The endpoint location to add the handler.
   * @param handler a handler object.
   */
  public void addHandler(String uri, CameraRequestHandler handler) {
    handlers.put(uri, handler);
  }


  @Override
  boolean isAllowed(Camera cam) {
    return cam != null;
  }
}
