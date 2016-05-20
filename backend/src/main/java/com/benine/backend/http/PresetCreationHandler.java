package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Class allows creation of a preset by tagging a camera viewpoint location.
**/
public class PresetCreationHandler  extends RequestHandler {
  
  /**
     * Handles a request of making a new preset. 
     * @param exchange the exchange containing data about the request.
     * @throws IOException when an error occurs with responding to the request.
  */
  public void handle(HttpExchange exchange) throws IOException {
    try {
      int cameraID = getCameraId(exchange);
      Camera camera = getCameraController().getCameraById(cameraID);
            
      if (camera instanceof IPCamera) {
        IPCamera ipCamera = (IPCamera)camera;
        
        Preset preset = createPreset(ipCamera);
        
        //Adding the new preset to the database
        getCameraController().addPreset(cameraID, preset);
        respondSuccess(exchange);
      
      }
    } catch (SQLException e) {
      respondFailure(exchange);
      getLogger().log("Preset can not be added to the database", LogEvent.Type.CRITICAL);
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
      int focus = ipCamera.getFocusPosition();
      int iris = ipCamera.getIrisPosition();
      int panspeed = 15;
      int tiltspeed = 1 ;
      boolean autoiris = ipCamera.isAutoIrisOn();
      boolean autofocus = ipCamera.isAutoFocusOn();
    
      //Create new Preset and return it.
      //TODO add image of just created preset
      Preset preset = new Preset(new Position(pan,tilt),zoom,
          focus,iris,autofocus, panspeed, tiltspeed, autoiris);
      
      return preset; 
      
    } catch (CameraConnectionException e) {
      getLogger().log("Camera is not an IPCamera", LogEvent.Type.CRITICAL);
    }
    
    return null;
  }
  
}
