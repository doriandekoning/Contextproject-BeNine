package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
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
  public PresetHandler(CameraController controller, Logger logger) {
    super(controller, logger);
  }


  /**
   * Handles a request
   * @param exchange the exchange containing data about the request.
   * @throws IOException when an error occurs with responding to the request.
   */
  public void handle(HttpExchange exchange) throws IOException {
    getLogger().log("Got an http request with uri: " + exchange.getRequestURI(), LogEvent.Type.INFO);
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response =  "";
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
    } catch (Exception e) {
      getLogger().log("Malformed URI: " + exchange.getRequestURI(), LogEvent.Type.WARNING);
      respond(exchange, "{\"succes\":\"false\"}");
      return;
    }
    int cameraId = getCameraId(exchange);
    // Used for retrieving presets from database
    ArrayList<DatabasePreset> presets = new ArrayList<DatabasePreset>();

    // TODO GET THE PRESETS FROM THE DATABASE HERE and put them in the preset list


    JSONArray json = new JSONArray();
    for (DatabasePreset preset : presets) {
      json.add(preset.toJSON());
    }
    response = new JSONObject().put("presets", json.toString()).toString();

  }
}
