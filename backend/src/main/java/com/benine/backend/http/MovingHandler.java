package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.sun.net.httpserver.HttpExchange;
import com.benine.backend.Main;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class MovingHandler extends RequestHandler {

  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
      Camera cam = Main.getCameraController().getCameraById(Integer.parseInt(parsedURI.getValue("id")));
      MovingCamera zoomingCam = (MovingCamera)cam;
      String moveType = parsedURI.getValue("moveType");
      String pan = parsedURI.getValue("pan");
      String tilt = parsedURI.getValue("tilt");
      String panSpeed = parsedURI.getValue("panSpeed");
      String tiltSpeed = parsedURI.getValue("tiltSpeed");
      if(pan == null || tilt == null || panSpeed == null || tiltSpeed == null) {
        throw new MalformedURIException("Invalid value for moveX or moveY");
      }
      if(moveType.equals("relative")) {
        zoomingCam.move(Integer.parseInt(pan), Integer.parseInt(tilt));
      } else if(moveType.equals("absolute")) {
        Position pos = new Position(Integer.parseInt(pan), Integer.parseInt(tilt));
        zoomingCam.moveTo(pos, Integer.parseInt(panSpeed), Integer.parseInt(tiltSpeed));
      } else {
        throw new MalformedURIException("Invalid value for zoom or zoomType invalid");
      }

    } catch (Exception e) {
      //TODO Log exception
      String response = "{\"succes\":\"false\"}";
      respond(exchange, response);
      return;
    }
  }
}
