package com.benine.backend.http;

import com.benine.backend.Main;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.database.DatabasePreset;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * @author naomi
 * Class allows creation of a preset by tagging a camera viewpoint location.
**/
public class CreatingPresetsHandler extends RequestHandler {
  

  /**
   * Create a new handler for creating new presets.
   * @param controller the controller to interact with.
   */
  public CreatingPresetsHandler(CameraController controller) {
    super(controller);
  }
  
  /**
     * Handles a request
     * @param exchange the exchange containing data about the request.
     * @throws IOException when an error occurs with responding to the request.
  */
  public void handle(HttpExchange exchange) throws IOException {
    Attributes parsedURI;
    String response = "{\"succes\":\"false\"}";
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
    } catch (MalformedURIException e) {
      respond(exchange, response);
      return;
    }
    
    //Get the values as String needed to create a preset. 
    String idStr = parsedURI.getValue("id");
    String panStr = parsedURI.getValue("pan");
    String tiltStr = parsedURI.getValue("tilt");
    String zoomStr = parsedURI.getValue("zoom");
    String focusStr = parsedURI.getValue("focus");
    String irisStr = parsedURI.getValue("iris");
    String autofocusStr = parsedURI.getValue("autoFocusOn");
    
    //Strings to integers and boolean to be able to create new preset. 
    int id = Integer.parseInt(idStr);
    int pan = Integer.parseInt(panStr);
    int tilt = Integer.parseInt(tiltStr);
    int zoom = Integer.parseInt(zoomStr);
    int focus = Integer.parseInt(focusStr);
    int iris = Integer.parseInt(irisStr);
    boolean autofocus = Boolean.parseBoolean(autofocusStr);
    
    //Create new DatabasePreset
    DatabasePreset preset = new DatabasePreset(pan,tilt,zoom,focus,iris,autofocus);
     
    //Adding the new preset to the database
    Main.getDatabase().addPreset(id, 4, preset);
 

  }
}
