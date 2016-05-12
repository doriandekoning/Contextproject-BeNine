package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandler extends RequestHandler {

  /**
   * Creates a new FocussingHandler.
   * @param controller the cameracontroller to interact with
   */
  public CameraInfoHandler(CameraController controller, Logger logger) {
    super(controller, logger);
  }

  /**
   * Handles incoming httprequest.
   * @param exchange the exchange containing data about the request and response.
   * @throws IOException When writing the response fails.
   */
  public void handle(HttpExchange exchange) throws IOException {
    getLogger().log("Got an http request with uri: " + exchange.getRequestURI(), LogEvent.Type.INFO);
    JSONObject jsonObj = new JSONObject();
    ArrayList<Camera> cameras = getCameraController().getCameras();
    JSONArray camerasJSON = new JSONArray();
    for (Camera cam : cameras) {
      try {
        camerasJSON.add(cam.toJSON());
      } catch (CameraConnectionException e) {
        getLogger().log("Cannot connect to camera: " + cam.getId(), LogEvent.Type.CRITICAL);
      }
    }
    jsonObj.put("cameras", camerasJSON);
    String response = jsonObj.toString();
    respond(exchange, response);
  }
}
