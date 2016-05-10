package com.benine.backend.http;

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
  public CameraInfoHandler(CameraController controller, int cameraId) {
    super(controller, cameraId);
  }

  /**
   * Handles incoming httprequest.
   * @param exchange the exchange containing data about the request and response.
   * @throws IOException When writing the response fails.
   */
  public void handle(HttpExchange exchange) throws IOException {
    // TODO give all camera info on /camera request
    //TODO add logging stuff
    JSONObject jsonObj = new JSONObject();
    ArrayList<Camera> cameras = getCameraController().getCameras();
    JSONArray camerasJSON = new JSONArray();
    for (Camera cam : cameras) {
      try {
        camerasJSON.add(cam.toJSON());
      } catch (CameraConnectionException e) {
        //TODO Log here when logger supports asyncronous logging
        //CameraController.logger
        //.log("Cannot connect to camera with id: " + cam.getId(), LogEvent.Type.CRITICAL);
        System.out.println(e.toString());
      }
    }
    jsonObj.put("cameras", camerasJSON);
    String response = jsonObj.toString();
    respond(exchange, response);
  }
}
