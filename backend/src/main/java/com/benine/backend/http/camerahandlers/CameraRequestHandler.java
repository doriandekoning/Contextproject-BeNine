package com.benine.backend.http.camerahandlers;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.http.RequestHandler;
import com.benine.backend.video.StreamController;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles all requests requiring the camera ID.
 */
public abstract class CameraRequestHandler extends RequestHandler {
  
  private Logger logger;
  
  private CameraController cameraController;
  
  private StreamController streamController;
  
  private Boolean streamCompression;

  /**
   * CameraRequest handler for the httpserver.
   * @param httpserver to construct the cameraHandler for.
   */
  public CameraRequestHandler(HTTPServer httpserver) {
    super(httpserver);
    logger = httpserver.getLogger();
    cameraController = httpserver.getCameraController();
    streamController = httpserver.getStreamController();
    streamCompression = httpserver.getConfig().getValue("stream_compression").equals("true");
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
  
  /**
   * Get the stream reader of the camera with camID.
   * @param camID of the stream to find.
   * @return the right streamreader.
   */
  public StreamReader getStreamReader(int camID) {
    try {
      return streamController.getStreamReader(camID);
    } catch (StreamNotAvailableException e) {
      getLogger().log("No stream available for this camera.", e);
    }
    return null;
  }
  
  public Logger getLogger() {
    return logger;
  }
  
  protected CameraController getCameraController() {
    return cameraController;
  }

  public Boolean isStreamCompression() {
    return streamCompression;
  }

}
