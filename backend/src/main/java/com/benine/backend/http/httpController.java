package com.benine.backend.http;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by dorian on 10-5-16.
 */
public class httpController {

  private HttpServer server;
  private Logger logger;
  private CameraController camController;

  /**
   * Constructor, creates the handlers for
   */
  public httpController(InetSocketAddress address, Logger logger, CameraController camController) {
    this.logger = logger;
    this.camController = camController;
    try {
      HttpServer server = HttpServer.create(address, 20);
      // TODO create endpoint like /camera/ to return all camera info
      // TODO create endpoints like /camera/1/ to return camera 1 info
      // TODO create handlers a handler for every camera
      // TODO move handlers to httpHandlerController
      server.createContext("/camera/0/getcamerainfo", new CameraInfoHandler(camController, 0));
      server.createContext("/camera/0/focus", new FocussingHandler(camController, 0));
      server.createContext("/camera/0/iris", new IrisHandler(camController, 0));
      server.createContext("/camera/0/move", new MovingHandler(camController, 0));
      server.createContext("/camera/0/zoom", new ZoomingHandler(camController, 0));
      server.createContext("/camera/0/preset", new PresetHandler(camController, 0));

      logger.log("Server running at: " + server.getAddress(), LogEvent.Type.INFO);
      server.start();
      while (true) {
        Thread.sleep(100);
      }
    } catch (IOException e) {
      logger.log("Unable to start server", LogEvent.Type.CRITICAL);
    } catch (InterruptedException e) {
      logger.log("Unable to start server", LogEvent.Type.CRITICAL);
    }

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
