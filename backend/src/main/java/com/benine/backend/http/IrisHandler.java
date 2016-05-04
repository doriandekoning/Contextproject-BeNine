package com.benine.backend.http;

import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class IrisHandler extends RequestHandler {
  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response;
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
      Camera cam = Main.getCameraController().getCameraById(Integer.parseInt(parsedURI.getValue("id")));
      IrisCamera irisCam = (IrisCamera)cam;
      String autoOn = parsedURI.getValue("autoIrisOn");
      String setPos = parsedURI.getValue("setPos");
      if(autoOn != null) {
        boolean autoOnBool = Boolean.parseBoolean(autoOn);
        irisCam.setAutoIrisOn(autoOnBool);
      }
      if(setPos != null) {
        irisCam.setIrisPos(Integer.parseInt(setPos));
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
