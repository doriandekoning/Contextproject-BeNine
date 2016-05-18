package com.benine.backend.http;


import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.sun.corba.se.spi.activation.Server;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
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


    try {
      Attributes parsedURI = parseURI(exchange.getRequestURI().getQuery());
      JSONArray json = new JSONArray();
      String tag = parsedURI.getValue("tag");
      ArrayList<Preset> presets = ServerController.getInstance()
              .getPresetController().getPresetsByTag(tag);
      for (Preset preset : presets) {
        json.add(preset.toJSON());
      }

      
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("presets", json);
      response = jsonObject.toString();
      
    } catch (MalformedURIException e) {
      getLogger().log("URI is malformed: " + exchange.getRequestURI(), LogEvent.Type.WARNING);
      response = "{\"succes\":\"false\"}";
    }
    respond(exchange, response);
  }
}
