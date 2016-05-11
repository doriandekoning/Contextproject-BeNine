package com.benine.backend.http;

import java.io.IOException;
import java.util.Random;
import java.util.jar.Attributes;

import com.benine.backend.LogEvent;
import com.benine.backend.Main;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.DatabasePreset;
import com.sun.net.httpserver.HttpExchange;

public class RecallPreset extends RequestHandler {
  
  /**
   * Create a new handler for recalling presets.
   * @param controller the controller to interact with.
   */
  public RecallPreset(CameraController controller) {
    super(controller);
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
        
      int cameraID = Integer.parseInt(parsedURI.getValue("id"));
      Random randomGenerator = new Random();
      int randomInt = randomGenerator.nextInt(100);
      DatabasePreset preset = Main.getDatabase().getPreset(cameraID,randomInt);
      Position position = new Position(preset.getPan(),preset.getTilt());
     
      IPCamera ipcamera =  (IPCamera)getCameraController().getCameraById(cameraID);
     
      //Moving the camera in the correct direction with the correct 
      //zoom, focus, autofocus, autoiris and speeds.
      ipcamera.zoomTo(preset.getZoom());
      ipcamera.moveTo(position, 15 , 1);
      ipcamera.moveFocus(preset.getFocus());
      ipcamera.setAutoFocusOn(preset.isAutofocus());
      ipcamera.setIrisPos(preset.getIris());
      ipcamera.setAutoIrisOn(true);
    } catch (MalformedURIException e) {
      responseMessage(exchange, false);
      Main.getLogger().log("Wrong URI", LogEvent.Type.CRITICAL);
      return;
    } catch (CameraConnectionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }   
      
  }
}
