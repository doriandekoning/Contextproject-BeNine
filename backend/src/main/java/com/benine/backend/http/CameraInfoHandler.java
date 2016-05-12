package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandler extends RequestHandler {

  /**
   * Creates a new FocussingHandler.
   * @param controller the cameracontroller to interact with
   */
  public CameraInfoHandler(CameraController controller, Logger logger) {
    super(controller, logger);
  }

  /**
   * Handles incoming httprequest.
   * @param exchange the exchange containing data about the request and response.
   * @throws IOException When writing the response fails.
   */
  public void handle(HttpExchange exchange) throws IOException {
    getLogger().log("Got an http request with uri: " + exchange.getRequestURI(), LogEvent.Type.INFO);
    try {
      respond(exchange, getCameraController().getCamerasJSON());
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to one of the cameras", LogEvent.Type.CRITICAL);
    }
    respond(exchange, "{\"succes\":false}");
  }
}
