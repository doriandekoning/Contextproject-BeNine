package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by dorian on 10-5-16.
 */
public class HttpController {

  private HttpServer server;
  private Logger logger;
  private CameraController camController;

  /**
   * Constructor, creates a new HttpController object.
   * @param address an internetsocet adress indicating the ports for the server to listen to.
   * @param logger the logger to use to log to.
   * @param camController the cameracontroller that contains
   *                      the camera's which this server interacts with.
   */
  public HttpController(InetSocketAddress address, Logger logger, CameraController camController) {
    this.logger = logger;
    this.camController = camController;
    try {
      server = HttpServer.create(address, 20);

      createHandlers();
      logger.log("Server running at: " + server.getAddress(), LogEvent.Type.INFO);
      server.start();
    } catch (IOException e) {
      logger.log("Unable to start server", LogEvent.Type.CRITICAL);
      e.printStackTrace();
    }

    setupBasicHandlers();
  }

  /**
   * Creates the basic handlers for endpoints like /camera/.
   */
  private void setupBasicHandlers() {
    server.createContext("/camera/", new CameraInfoHandler(camController, logger));
  }
  
  /**
   * Creates handlers for all cams in the camera controller.
   */
  private void createHandlers() {
    server.createContext("/camera/", new CameraInfoHandler(camController, logger));
    for (Camera cam : camController.getCameras()) {
      createHandlers(cam);
    }
  }
  
  /**
   * Creates the handlers for a certain camera.
   * @param cam camera to create handlers for.
   */
  private void createHandlers(Camera cam) {
    int camId = cam.getId();
    if (cam instanceof FocussingCamera) {
      server.createContext("/camera/" + camId
              + "/focus", new FocussingHandler(camController, logger));
    }
    if (cam instanceof IrisCamera) {
      server.createContext("/camera/" + camId + "/iris", new IrisHandler(camController, logger));
    }
    if (cam instanceof MovingCamera) {
      server.createContext("/camera/" + camId + "/move", new MovingHandler(camController, logger));
    }
    if (cam instanceof ZoomingCamera) {
      server.createContext("/camera/" + camId + "/zoom", new ZoomingHandler(camController, logger));
    }
    server.createContext("/camera/" + camId + "/preset", new PresetHandler(camController, logger));
    server.createContext("/camera/" + camId + "/createpreset", 
                                                 new PresetCreationHandler(camController, logger));
    server.createContext("/camera/" + camId + "/recallPreset",
                                                   new RecallPresetHandler(camController, logger));

    logger.log("Succesufully setup endpoints", LogEvent.Type.INFO);
  }

  /**
   * Destroys http handler.
   */
  public void destroy() {
    server.stop(0);
  }
}
