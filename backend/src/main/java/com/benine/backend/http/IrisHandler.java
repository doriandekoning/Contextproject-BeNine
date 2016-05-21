package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.http.jetty.MalformedURIException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Handles an command from the client to change the iris of a camera.
 * Created on 4-5-16.
 */
@Deprecated
public class IrisHandler extends RequestHandler {
  
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
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
    } catch (MalformedURIException exception) {
      getLogger().log("Malformed URI: " + exchange.getRequestURI(), LogEvent.Type.WARNING);
      respondFailure(exchange);
      return;
    }
    int camId = getCameraId(exchange);

    Camera cam =  getCameraController().getCameraById(camId);
    IrisCamera irisCam = (IrisCamera)cam;
    String autoOn = parsedURI.getValue("autoIrisOn");
    String setPos = parsedURI.getValue("position");
    String speed = parsedURI.getValue("speed");
    try {
      if (autoOn != null) {
        boolean autoOnBool = Boolean.parseBoolean(autoOn);
        irisCam.setAutoIrisOn(autoOnBool);
      }
      if (setPos != null) {
        irisCam.setIrisPosition(Integer.parseInt(setPos));
      } else if (speed != null) {
        irisCam.moveIris(Integer.parseInt(speed));
      }
    } catch (Exception e) {
      getLogger().log("Cannot connect with camera: " + cam.getId(), LogEvent.Type.WARNING);
      respondFailure(exchange);
    }
    respondSuccess(exchange);
  }
}
