package com.benine.backend.http;

import java.io.IOException;
import java.util.Random;
import java.util.jar.Attributes;

import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.DatabasePreset;
import com.sun.net.httpserver.HttpExchange;

/**
 * @author naomi
 * Class allows creation of a preset by tagging a camera viewpoint location.
**/
public class PresetCreationHandler  extends RequestHandler {
  
  private static 
  
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
      }
        
      int zoom = ((ZoomingCamera)ipCamera).getZoomPosition();
      int pan = (int)ipCamera.getPosition().getPan();
      int tilt = (int)ipCamera.getPosition().getTilt();
      int focus = ((FocussingCamera)ipCamera).getFocusPos();
      int iris = ((IrisCamera)ipCamera).getIrisPos();
      boolean autofocus = ipCamera.isAutoFocusOn();
    
      //Create new DatabasePreset.
      DatabasePreset preset = new DatabasePreset(pan,tilt,zoom,focus,iris,autofocus);
      
      //Create a random integer for the preset number, should later be changed.
      Random randomGenerator = new Random();
      int randomInt = randomGenerator.nextInt(100);
      //Adding the new preset to the database
      Main.getDatabase().addPreset(cameraID, randomInt, preset);
      
    } catch (MalformedURIException e) {
      respond(exchange, response);
      return;
    } catch (CameraConnectionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    //Get the values as String needed to create a preset. 
    
//    String idStr = parsedURI.getValue("id");
//    String panStr = parsedURI.getValue("pan");
//    String tiltStr = parsedURI.getValue("tilt");
//    String zoomStr = parsedURI.getValue("zoom");
//    String focusStr = parsedURI.getValue("focus");
//    String irisStr = parsedURI.getValue("iris");
//    String autofocusStr = parsedURI.getValue("autoFocusOn");
    
    //Strings to integers and boolean to be able to create new preset. 
//    int id = Integer.parseInt(idStr);
//    int pan = Integer.parseInt(panStr);
//    int tilt = Integer.parseInt(tiltStr);
//    int zoom = Integer.parseInt(zoomStr);
//    int focus = Integer.parseInt(focusStr);
//    int iris = Integer.parseInt(irisStr);
//    boolean autofocus = Boolean.parseBoolean(autofocusStr);
    
    
   
// 

  }
}
