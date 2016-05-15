package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
// Add superclass requesthandler
public class ZoomingHandler extends RequestHandler {

  /**
   * Creates a new FocussingHandler.
   * @param server the server controller to interact with
   * @param logger the logger to be used to log to
   */
  public ZoomingHandler(ServerController serverController, Logger logger) {
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
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response;
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());


      Camera cam = getServerController().getCameraController().getCameraById(getCameraId(exchange));
      ZoomingCamera zoomingCam = (ZoomingCamera)cam;
      String zoomto = parsedURI.getValue("zoomType");
      String zoom = parsedURI.getValue("zoom");
      if (zoom != null && zoomto.equals("relative")) {
        zoomingCam.zoom(Integer.parseInt(zoom));
      } else if (zoom != null && zoomto.equals("absolute")) {
        zoomingCam.zoomTo(Integer.parseInt(zoom));
      } else {
        throw new MalformedURIException("Invalid value for zoom or zoomType invalid");
      }
      response = "{\"succes\":\"true\"}";
    } catch (MalformedURIException | CameraConnectionException e) {
      getLogger().log("Exception occured while respoinding to the request with URI: "
              + exchange.getRequestURI(), LogEvent.Type.WARNING);
      response = "{\"succes\":\"false\"}";
    }
    respond(exchange, response);

  }
}
