package com.benine.backend.http;


import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.Preset;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles a request to get information about presets.
 * Created on 4-5-16.
 */
public class PresetHandler extends RequestHandler {

  /**
   * Creates a new PresetHandler.
   * @param logger the logger to be used to log to
   */
  public PresetHandler(Logger logger) {
    super(logger);
  }


  /**
   * Handles a request
   * @param exchange the exchange containing data about the request.
   * @throws IOException when an error occurs with responding to the request.
   */
  public void handle(HttpExchange exchange) throws IOException {
    getLogger().log("Got an http request with uri: "
            + exchange.getRequestURI(), LogEvent.Type.INFO);

    int cameraId = getCameraId(exchange);
    String response =  "";
    
    ArrayList<Preset> presets = new ArrayList<Preset>();

    try {
      if (getDatabase() != null) {
        presets = getDatabase().getAllPresetsCamera(cameraId);
      }
        
      JSONArray json = new JSONArray();
      for (Preset preset : presets) {
        json.add(preset.toJSON());
      }
      
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("presets", json);
      response = jsonObject.toString();
      
    } catch (SQLException e) {
      getLogger().log("Exception occured while respoinding to the request with URI: "
          + exchange.getRequestURI(), LogEvent.Type.WARNING);
      respondFailure(exchange);
    }
    respond(exchange, response);
  }
}
