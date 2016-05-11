package com.benine.backend.http;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.*;
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
      server = HttpServer.create(address, 20);

      createHandlers();
      logger.log("Server running at: " + server.getAddress(), LogEvent.Type.INFO);
      server.start();
    } catch (IOException e) {
      logger.log("Unable to start server", LogEvent.Type.CRITICAL);
    }

    // TODO Create server
    setupBasicHandlers();
  }

  /**
   * Creates the basic handlers for endpoints like /camera/.
   */
  private void setupBasicHandlers() {
    server.createContext("/camera/", new CameraInfoHandler(camController));
  }
  /**
   * Creates handlers for all cams in the camera controller.
   */
  private void createHandlers() {
    for(Camera cam : camController.getCameras()){
      createHandlers(cam);
    }
  }
  /**
   * Creates the handlers for a certain camera.
   * @param cam camera to create handlers for.
   */
   public void createHandlers(Camera cam) {
    int camId = cam.getId();
    server.createContext("/camera/", new CameraInfoHandler(camController));
    if (cam instanceof FocussingCamera) {
      server.createContext("/camera/" + camId + "/focus", new FocussingHandler(camController));
    }
    if (cam instanceof IrisCamera) {
      server.createContext("/camera/" + camId + "/iris", new IrisHandler(camController));
    }
    if (cam instanceof MovingCamera) {
      server.createContext("/camera/" + camId + "/move", new MovingHandler(camController));
    }
    if (cam instanceof ZoomingCamera) {
      server.createContext("/camera/" + camId + "/zoom", new ZoomingHandler(camController));
    }
    server.createContext("/camera/" + camId + "/preset", new PresetHandler(camController));
  }

  /**
   * Destroys http handler.
   */
  public void destroy() {
    server.stop(0);
  }
  /**
   * Getter for httpserver.
   */
  public HttpServer getServer() {
    return server;
  }
  /**
   * Setter for httpserver.
   */
  public void setServer(HttpServer server) {
    this.server = server;
  }
}
