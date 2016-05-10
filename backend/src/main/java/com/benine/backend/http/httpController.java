package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.sun.net.httpserver.HttpServer;

/**
 * Created by dorian on 10-5-16.
 */
public class httpController {

  private HttpServer server;

  /**
   * Constructor, creates the handlers for
   */
  public httpController() {
    // TODO Create server
    setupBasicHandlers();
  }

  /**
   * Creates the basic handlers for endpoints like /camera/.
   */
  private void setupBasicHandlers() {
    // TODO setup basic handlers
  }
  /**
   * Creates the handlers for a certain camera.
   * @param cam camera to create handlers for.
   */
  public void createHandlers(Camera cam) {
    //TODO test and implement
  }

}
