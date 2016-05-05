package com.benine.backend.http;

import com.benine.backend.Main;
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

  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response;
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
      Camera cam = Main.getCameraController().getCameraById(Integer.parseInt(parsedURI.getValue("id")));
      ZoomingCamera zoomingCam = (ZoomingCamera)cam;
      String zoomto = parsedURI.getValue("zoomType");
      String zoom = parsedURI.getValue("zoom");
      if(zoom != null && zoomto.equals("relative")) {
        zoomingCam.zoom(Integer.parseInt(zoom));
      } else if(zoom != null && zoomto.equals("absolute")) {
        zoomingCam.zoomTo(Integer.parseInt(zoom));
      } else {
        throw new MalformedURIException("Invalid value for zoom or zoomType invalid");
      }
      response = "{\"succes\":\"true\"}";
    } catch (MalformedURIException e) {
      //TODO Log exception
      response = "{\"succes\":\"false\"}";
    } catch (CameraConnectionException e) {
      // TODO Log exception
      response = "{\"succes\":\"false\"}";
    }
    respond(exchange, response);

  }
}
