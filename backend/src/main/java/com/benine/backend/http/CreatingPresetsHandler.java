package com.benine.backend.http;

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
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
      //Get the values needed to create a preset. 
      String id = parsedURI.getValue("id");
      String pan = parsedURI.getValue("pan");
      String tilt = parsedURI.getValue("tilt");
      String zoom = parsedURI.getValue("zoom");
      String focus = parsedURI.getValue("focus");
      String iris = parsedURI.getValue("iris");
      String autofocus = parsedURI.getValue("autoFocusOn");
    }
    
    catch (MalformedURIException e) {
      //TODO Log exception
      System.out.println(e);
    }

  }
}
