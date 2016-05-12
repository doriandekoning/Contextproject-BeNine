package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.Main;
import com.benine.backend.Preset;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.jar.Attributes;

/**
 * Class allows creation of a preset by tagging a camera viewpoint location.
**/
public class PresetCreationHandler  extends RequestHandler {
  
 
  /**
   * Create a new handler for creating new presets.
   * @param controller the controller to interact with.
   */
  public PresetCreationHandler(CameraController controller, Logger logger) {
    super(controller, logger);
  }
  
  /**
     * Handles a request of making a new preset. 
     * @param exchange the exchange containing data about the request.
     * @throws IOException when an error occurs with responding to the request.
  */
  public void handle(HttpExchange exchange) throws IOException {
    Attributes parsedURI;
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
        
      int cameraID = getCameraId(exchange);
      Camera camera =  getCameraController().getCameraById(cameraID);
            
      if (camera instanceof IPCamera) {
        IPCamera ipCamera = (IPCamera)camera;
        
        Preset preset = createPreset(ipCamera);
                
        //Create a random integer for the preset number.
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);
        //Adding the new preset to the database
        Main.getDatabase().addPreset(cameraID, randomInt, preset);
        responseSuccess(exchange);
      
      }
    } catch (MalformedURIException e) {
      responseFailure(exchange);
      Main.getLogger().log("Wrong URI", LogEvent.Type.CRITICAL);
      return;
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
  
  /**
   * @param ipCamera the ipCamera you want to get the position of.
   * @return DatabasePreset preset.
   */
  public Preset createPreset(IPCamera ipCamera) {
    try {
      //Get everything that is needed to create a new preset.  
      int zoom = ipCamera.getZoomPosition();
      int pan = (int)ipCamera.getPosition().getPan();
      int tilt = (int)ipCamera.getPosition().getTilt();
      int focus = ipCamera.getFocusPos();
      int iris = ipCamera.getIrisPos();
      int panspeed = 15;
      int tiltspeed = 1 ;
      boolean autoiris = ipCamera.isAutoIrisOn();
      boolean autofocus = ipCamera.isAutoFocusOn();
    
      //Create new Preset and return it.
      return new Preset(pan,tilt,zoom,focus,iris,autofocus, panspeed, tiltspeed, autoiris);
      
    } catch (CameraConnectionException e) {
      Main.getLogger().log("Camera is not an IPCamera", LogEvent.Type.CRITICAL);
    }
    return null;
  }
  
}
