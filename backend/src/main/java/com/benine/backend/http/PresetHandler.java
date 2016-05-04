package com.benine.backend.http;

import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.benine.backend.database.Preset;
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
  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response =  "";
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
      String cameraId = parsedURI.getValue("cameraId");
      if(cameraId!=null){
        int id = Integer.parseInt(cameraId);
        ArrayList<Preset> presets = new ArrayList<Preset>();

        // GET THE PRESETS FROM THE DATABASE HERE and put them in the preset list


        JSONArray json = new JSONArray();
        for(Preset preset : presets) {
          json.add(preset.toJSON());
        }
        response = new JSONObject().put("presets", json.toString()).toString();
      }
    } catch (Exception e) {
      //TODO Log exception
      response = "{\"succes\":\"false\"}";
    }
    respond(exchange, response);
    return;
  }
}
