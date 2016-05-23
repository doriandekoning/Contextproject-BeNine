package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Controls all the HTTP endpoints of the backend server.
 * Created on 10-5-16.
 */
public class HttpController {

  private HttpServer server;

  /**
   * Constructor, creates a new HttpController object.
   * @param address an internetsocet address indicating the ports for the server to listen to.
   * @param port number to connect to.
   */
  public HttpController(String address, int port) {
    this(createServer(address, port));
  }

  /**
   * Constructor, creates a new HttpController object.
   * @param httpserver a server object.
   */
  public HttpController(HttpServer httpserver) {
    this.server = httpserver;
    createHandlers();
    server.start();
    ServerController.getInstance().getLogger()
          .log("Server running at: " + server.getAddress(), LogEvent.Type.INFO);
  }

  /**
   * Creates a server object.
   * @param address Socket address
   * @param port port number to connect to
   * @return  An HttpServer.
     */
  private static HttpServer createServer(String address, int port) {
    try {
      InetSocketAddress socket = new InetSocketAddress(address, port);
      return HttpServer.create(socket, 20);
    } catch (IOException e) {
      ServerController.getInstance().getLogger()
                          .log("Unable to create server", LogEvent.Type.CRITICAL);
      e.printStackTrace();
      return null;
    }
  }
  
  /**
   * Creates handlers for all cams in the camera controller.
   */
  private void createHandlers() {
    server.createContext("/static", new FileHandler());
    server.createContext("/camera/", new CameraInfoHandler());
    for (Camera cam : ServerController.getInstance().getCameraController().getCameras()) {
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
              + "/focus", new FocussingHandler());
    }
    if (cam instanceof IrisCamera) {
      server.createContext("/camera/" + camId + "/iris",
              new IrisHandler());
    }
    if (cam instanceof MovingCamera) {
      server.createContext("/camera/" + camId + "/move",
              new MovingHandler());
    }
    if (cam instanceof ZoomingCamera) {
      server.createContext("/camera/" + camId + "/zoom",
              new ZoomingHandler());
    }
    server.createContext("/presets/", new PresetHandler());
    server.createContext("/presets/createpreset", new PresetCreationHandler());
    server.createContext("/presets/recallpreset", new RecallPresetHandler());

    ServerController.getInstance().getLogger()
                          .log("Succesfully setup endpoints", LogEvent.Type.INFO);
  }

  /**
   * Destroys http handler.
   */
  public void destroy() {
    server.stop(0);
  }
}
