package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.FocussingCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class FocussingHandler extends RequestHandler {

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
    } catch (MalformedURIException e) {
      getLogger().log("Mallformed URI: " + exchange.getRequestURI(), LogEvent.Type.WARNING);
      respondFailure(exchange);
      return;
    }
    Camera cam = getCameraController().getCameraById(getCameraId(exchange));
    FocussingCamera focusCam = (FocussingCamera) cam;
    String autoOn = parsedURI.getValue("autoFocusOn");
    String setPos = parsedURI.getValue("position");
    String speed = parsedURI.getValue("speed");
    try {
      if (autoOn != null) {
        boolean autoOnBool = Boolean.parseBoolean(autoOn);
        focusCam.setAutoFocusOn(autoOnBool);
      }
      if (setPos != null) {
        focusCam.setFocusPosition(Integer.parseInt(setPos));
      } else if (speed != null) {
        focusCam.moveFocus(Integer.parseInt(speed));
      }
      respondSuccess(exchange);
    } catch (CameraConnectionException e) {
      respondFailure(exchange);
    }
  }
}
