package com.benine.backend.http;

import java.io.IOException;
import java.util.Random;
import java.util.jar.Attributes;

import com.benine.backend.LogEvent;
import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.DatabasePreset;
import com.sun.net.httpserver.HttpExchange;

/**
 * @author naomi
 * Class allows creation of a preset by tagging a camera viewpoint location.
**/
public class PresetCreationHandler  extends RequestHandler {
  
 
  /**
   * Create a new handler for creating new presets.
   * @param controller the controller to interact with.
   */
  public PresetCreationHandler(CameraController controller) {
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
        
      int cameraID = Integer.parseInt(parsedURI.getValue("id"));
      Camera camera =  getCameraController().getCameraById(cameraID);
      
      IPCamera ipCamera = null;
      
      if (camera instanceof IPCamera) {
        ipCamera = (IPCamera)camera;
             
        //Get everything that is needed to create a new preset.  
        int zoom = ipCamera.getZoomPosition();
        int pan = (int)ipCamera.getPosition().getPan();
        int tilt = (int)ipCamera.getPosition().getTilt();
        int focus = ipCamera.getFocusPos();
        int iris = ipCamera.getIrisPos();
        boolean autofocus = ipCamera.isAutoFocusOn();
      
        //Create new DatabasePreset.
        DatabasePreset preset = new DatabasePreset(pan,tilt,zoom,focus,iris,autofocus);
        
        //Create a random integer for the preset number, should later be changed.
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);
        //Adding the new preset to the database
        Main.getDatabase().addPreset(cameraID, randomInt, preset);
      }
    } catch (MalformedURIException e) {
      respond(exchange, response);
      return;
    } catch (CameraConnectionException e) {
      Main.getLogger().log("Camera is not an IPCamera", LogEvent.Type.CRITICAL);
    } catch (NullPointerException e) {
      Main.getLogger().log("Camera is not an IPCamera", LogEvent.Type.CRITICAL);
    }
    
    
  }
}
