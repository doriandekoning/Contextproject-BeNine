package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import org.eclipse.jetty.server.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles all requests requiring the camera ID.
 */
public abstract class CameraRequestHandler extends RequestHandler {

  /**
   * CameraRequest handler for the httpserver.
   * @param httpserver to construct the cameraHandler for.
   */
  public CameraRequestHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  /**
   * Defines if a camera is allowed to be used with this handler.
   * @param cam a Camera object.
   * @return boolean if the camera type is allowed for this handler.
   */
  abstract boolean isAllowed(Camera cam);

  /**
   * Fetches camera id from http exchange.
   * @param request the request to fix the id from.
   * @return the id of the camera.
   */
  public int getCameraId(Request request) {
    Pattern pattern = Pattern.compile("^/(\\d*)/.*");
    String path = request.getPathInfo();

    Matcher m = pattern.matcher(path);

    return m.matches() ? Integer.parseInt(m.group(1)) : -1;
  }

  /**
   * Returns the route of the url, so we can select the next handler.
   * @param request   The current request.
   * @return          Returns the route.
   */
  public String getRoute(Request request) {
    String path = request.getPathInfo();

    return path.replaceFirst(".*/(\\d*)/", "");
  }
}
