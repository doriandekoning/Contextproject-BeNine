package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.FocussingCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class FocussingHandler extends RequestHandler {

  /**
   * Creates a new FocussingHandler.
   * @param controller the cameracontroller to interact with
   * @param logger the logger to be used to log to
   */
  public FocussingHandler(CameraController controller, Logger logger) {
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
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
    } catch (MalformedURIException e) {
      getLogger().log("Mallformed URI: " + exchange.getRequestURI(), LogEvent.Type.WARNING);
      responseFailure(exchange);
      return;
    }
    Camera cam = getCameraController().getCameraById(getCameraId(exchange));
    FocussingCamera focusCam = (FocussingCamera) cam;
    String autoOn = parsedURI.getValue("autoFocusOn");
    String setPos = parsedURI.getValue("position");
    try {
      if (autoOn != null) {
        boolean autoOnBool = Boolean.parseBoolean(autoOn);
        focusCam.setAutoFocusOn(autoOnBool);
      }
      if (setPos != null) {
        focusCam.setFocusPos(Integer.parseInt(setPos));
      }
    } catch (CameraConnectionException e) {
      responseFailure(exchange);
      return;
    }
    responseSuccess(exchange);

  }
}
