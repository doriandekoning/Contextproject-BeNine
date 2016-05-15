package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandler extends RequestHandler {

  /**
   * Creates a new FocussingHandler.
   * @param serverController the server controller to interact with
   * @param logger the logger to be used to log to
   */
  public CameraInfoHandler(ServerController serverController, Logger logger) {
    super(serverController, logger);
  }

  /**
   * Handles incoming httprequest.
   * @param exchange the exchange containing data about the request and response.
   * @throws IOException When writing the response fails.
   */
  public void handle(HttpExchange exchange) throws IOException {
    getLogger().log("Got an http request with uri: "
            + exchange.getRequestURI(), LogEvent.Type.INFO);
    try {
      respond(exchange, getServerController().getCameraController().getCamerasJSON());
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera with id: " + e.getCamId(), LogEvent.Type.CRITICAL);
    }
    respond(exchange, "{\"succes\":false}");
  }
}
