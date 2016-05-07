package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class MovingHandler extends RequestHandler {

  /**
   * Creates a new MovingHandler.
   * @param controller the cameracontroller to interact with
   */
  public MovingHandler(CameraController controller) {
    super(controller);
  }

  /**
   * Handles a request
   * @param exchange the exchange containing data about the request.
   * @throws IOException when an error occurs with responding to the request.
   */
  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response = "{\"succes\":\"false\"}";
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
      Camera cam = getCameraController().getCameraById(Integer.parseInt(parsedURI.getValue("id")));
      MovingCamera movingCam = (MovingCamera)cam;
      String moveType = parsedURI.getValue("moveType");
      String pan = parsedURI.getValue("pan");
      String tilt = parsedURI.getValue("tilt");
      String panSpeed = parsedURI.getValue("panSpeed");
      String tiltSpeed = parsedURI.getValue("tiltSpeed");
      if (pan == null || tilt == null || panSpeed == null || tiltSpeed == null ) {
        throw new MalformedURIException("Invalid value for moveX or moveY");
      }
      if (moveType.equals("relative")) {
        movingCam.move(Integer.parseInt(pan), Integer.parseInt(tilt));
      } else if (moveType.equals("absolute")) {
        Position pos = new Position(Integer.parseInt(pan), Integer.parseInt(tilt));
        movingCam.moveTo(pos, Integer.parseInt(panSpeed), Integer.parseInt(tiltSpeed));
      } else {
        throw new MalformedURIException("Invalid value for zoom or zoomType invalid");
      }
      response = "{\"succes\":\"true\"}";
    } catch (MalformedURIException e) {
      //TODO Log exception
      System.out.println(e);
    } catch (CameraConnectionException e) {
      //TODO log exeption
      System.out.println(e);
    }
    respond(exchange, response);
  }
}