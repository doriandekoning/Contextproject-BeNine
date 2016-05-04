package com.benine.backend.http;

import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
// Add superclass requesthandler
public class CameraZoomHandler extends RequestHandler {

  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    JSONObject jsonObj = new JSONObject();
    ArrayList<Camera> cameras = Main.getCameraController().getCameras();


    // Extract camera id from function and amount to zoom in
    try {
      Attributes parsedURI = RequestHandler.parseURI(exchange.getRequestURI().getQuery());
    } catch (MalformedURIException e) {
      //TODO Log exception
      String response = "{\"succes\":\"false\"}";
      exchange.sendResponseHeaders(200, response.length());
      OutputStream out = exchange.getResponseBody();
      out.write(response.getBytes());
      out.close();
      return;
    }
    //TODO zoom
    String response = "{\"succes\":\"false\"}";
    exchange.sendResponseHeaders(200, response.length());
    OutputStream out = exchange.getResponseBody();
    out.write(response.getBytes());
    out.close();
  }
}
