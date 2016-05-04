package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandler implements HttpHandler {

  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    JSONObject jsonObj = new JSONObject();
    ArrayList<Camera> cameras = Main.getCameraController().getCameras();
    JSONArray camerasJSON = new JSONArray();
    for(Camera cam : cameras) {
      try {
        camerasJSON.add(cam.toJSON());
      } catch (CameraConnectionException e) {
        CameraController.logger.log("Cannot connect to camera with id: " + cam.getId(), LogEvent.Type.CRITICAL)
      }
    }
    jsonObj.put("cameras", Main.getCameraController().getCameras());
  }
}
