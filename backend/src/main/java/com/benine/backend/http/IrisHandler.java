package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.IrisCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class IrisHandler extends RequestHandler {

  /**
   * Creates a new IrisHandler.
   * @param controller which controls the cameras.
   * @param logger the logger to be used to log to
   */
  public IrisHandler(CameraController controller, Logger logger) {
    super(controller, logger);
  }
  
  /**
   * Handles a request
   * @param exchange the exchange containing data about the request.
   * @throws IOException when an error occurs with responding to the request.
   */
  public void handle(HttpExchange exchange) throws IOException {
    getLogger().log("Got an http request with uri: "
            + exchange.getRequestURI(), LogEvent.Type.INFO);
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response = "{\"succes\":\"true\"}";
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
    } catch (MalformedURIException exception) {
      getLogger().log("Malformed URI: " + exchange.getRequestURI(), LogEvent.Type.WARNING);
      respond(exchange, "{\"succes\":\"false\"}");
      return;
    }
    int camId = getCameraId(exchange);

    Camera cam =  getCameraController().getCameraById(camId);
    IrisCamera irisCam = (IrisCamera)cam;
    String autoOn = parsedURI.getValue("autoIrisOn");
    String setPos = parsedURI.getValue("position");
    try {
      if (autoOn != null) {
        boolean autoOnBool = Boolean.parseBoolean(autoOn);
        irisCam.setAutoIrisOn(autoOnBool);
      }
      if (setPos != null) {
        irisCam.setIrisPosition(Integer.parseInt(setPos));
      }
    } catch (Exception e) {
      getLogger().log("Cannot connect with camera: " + cam.getId(), LogEvent.Type.WARNING);
      response = "{\"succes\":\"false\"}";
    }
    respond(exchange, response);
  }
}
