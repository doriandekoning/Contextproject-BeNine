package com.benine.backend.http.jetty;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.database.Database;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dorian on 4-5-16.
 */

public abstract class CameraRequestHandler extends AbstractHandler {

  private Logger logger;

  public CameraRequestHandler() {}

  /**
   * Returns cameracontroller
   * @return cameracontroller interacting with.
   */
  protected CameraController getCameraController() {
    return ServerController.getInstance().getCameraController();
  }
  
  /**
   * Returns the database
   * @return database to retrieve information from.
   */
  protected Database getDatabase() {
    return ServerController.getInstance().getDatabase();
  }


  /**
   * Fetches camera id from http exchange.
   * @param request the request to fix the id from.
   * @return the id of the camera.
   */
  public int getCameraId(Request request) {
    // Get path
    Pattern pattern = Pattern.compile(".*/(\\d*)/.*");
    String path = request.getPathInfo();

    try {
      Matcher m = pattern.matcher(path);
      m.matches();
      return Integer.parseInt(m.group(1));
    } catch (IllegalStateException e) {
      return -1;
    }
  }

  public String getRoute(Request request) {
    String path = request.getPathInfo();

    return path.replaceFirst(".*/(\\d*)/", "");
  }
}
