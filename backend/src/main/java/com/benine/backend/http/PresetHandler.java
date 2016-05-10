package com.benine.backend.http;

import com.benine.backend.camera.CameraController;
import com.benine.backend.database.DatabasePreset;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class PresetHandler extends RequestHandler {

  /**
   * Creates a new PresetHandler.
   * @param controller the cameracontroller to interact with
   */
  public PresetHandler(CameraController controller, int cameraId) {
    super(controller, cameraId);
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
    String response =  "";
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
    } catch (Exception e) {
      //TODO Log exception
      respond(exchange, "{\"succes\":\"false\"}");
      return;
    }
    String cameraId = parsedURI.getValue("cameraId");
    if (cameraId != null) {
      // Used for retrieving presets from database
      int id = Integer.parseInt(cameraId);
      ArrayList<DatabasePreset> presets = new ArrayList<DatabasePreset>();

      // GET THE PRESETS FROM THE DATABASE HERE and put them in the preset list


      JSONArray json = new JSONArray();
      for (DatabasePreset preset : presets) {
        json.add(preset.toJSON());
      }
      response = new JSONObject().put("presets", json.toString()).toString();
    }
    respond(exchange, response);
  }
}
