package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
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
  private ServerController serverController;

  /**
   * Constructor, creates a new HttpController object.
   * @param address an internetsocet address indicating the ports for the server to listen to.
   * @param logger the logger to use to log to.
   * @param serverController the server controller that contains
   *                      the camera's which this server interacts with.
   * @param port number to connect to.
   */
  public HttpController(String address, int port, Logger logger,
                                                        ServerController serverController) {
    this(createServer(address, port, logger), logger, serverController);
  }

  /**
   * Constructor, creates a new HttpController object.
   * @param httpserver a server object.
   * @param logger the logger to use to log to.
   * @param serverController the server controller that contains
   *                      the camera's which this server interacts with.
   */
  public HttpController(HttpServer httpserver, Logger logger, ServerController serverController) {
    this.logger = logger;
    this.serverController = serverController;
    this.server = httpserver;

    createHandlers();
    server.start();
    logger.log("Server running at: " + server.getAddress(), LogEvent.Type.INFO);
  }

  /**
   * Creates a server object.
   * @param address Socket address
   * @param logger  Logger
   * @param port port number to connect to
   * @return  An HttpServer.
     */
  private static HttpServer createServer(String address, int port, Logger logger) {
    try {
      InetSocketAddress socket = new InetSocketAddress(address, port);
      return HttpServer.create(socket, 20);
    } catch (IOException e) {
      logger.log("Unable to create server", LogEvent.Type.CRITICAL);
      e.printStackTrace();
      return null;
    }
  }
  
  /**
   * Creates handlers for all cams in the camera controller.
   */
  private void createHandlers() {
    server.createContext("/static", new FileHandler(serverController, logger));
    server.createContext("/camera/", new CameraInfoHandler(serverController, logger));
    for (Camera cam : serverController.getCameraController().getCameras()) {
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
              + "/focus", new FocussingHandler(serverController, logger));
    }
    if (cam instanceof IrisCamera) {
      server.createContext("/camera/" + camId + "/iris",
                                              new IrisHandler(serverController, logger));
    }
    if (cam instanceof MovingCamera) {
      server.createContext("/camera/" + camId + "/move", 
                                              new MovingHandler(serverController, logger));
    }
    if (cam instanceof ZoomingCamera) {
      server.createContext("/camera/" + camId + "/zoom",
                                              new ZoomingHandler(serverController, logger));
    }
    server.createContext("/camera/" + camId + "/preset", 
                                              new PresetHandler(serverController, logger));
    server.createContext("/camera/" + camId + "/createpreset", 
                                              new PresetCreationHandler(serverController, logger));
    server.createContext("/camera/" + camId + "/recallpreset",
                                               new RecallPresetHandler(serverController, logger));

    logger.log("Succesufully setup endpoints", LogEvent.Type.INFO);
  }

  /**
   * Destroys http handler.
   */
  public void destroy() {
    server.stop(0);
  }
}
