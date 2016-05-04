package com.benine.backend.http;

import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
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
    //TODO add logging stuff
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response;
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
      Camera cam = Main.getCameraController()
                      .getCameraById(Integer.parseInt(parsedURI.getValue("id")));
      FocussingCamera irisCam = (FocussingCamera) cam;
      String autoOn = parsedURI.getValue("autoFocusOn");
      String setPos = parsedURI.getValue("position");
      if (autoOn != null) {
        boolean autoOnBool = Boolean.parseBoolean(autoOn);
        irisCam.setAutoFocusOn(autoOnBool);
      }
      if (setPos != null) {
        irisCam.setFocusPos(Integer.parseInt(setPos));
      }
      response = "{\"succes\":\"true\"}";
    } catch (Exception e) {
      //TODO Log exception
      response = "{\"succes\":\"false\"}";
    }
    respond(exchange, response);
    return;

  }
}
