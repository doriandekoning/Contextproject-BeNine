package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.SQLException;
import java.util.jar.Attributes;


public class RecallPresetHandler extends RequestHandler {
  
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
      int presetID = Integer.parseInt(parsedURI.getValue("presetid"));
      Preset preset = getDatabase().getPreset(cameraID,presetID);
      IPCamera ipcamera = (IPCamera)getCameraController().getCameraById(cameraID);
      
      movingCamera(ipcamera,preset);
      respondSuccess(exchange);
    } catch (MalformedURIException e) {
      respondFailure(exchange);
      getLogger().log("Wrong URI", LogEvent.Type.CRITICAL);
    }  catch (SQLException | CameraConnectionException e) {
      getLogger().log("Preset can't be recalled: ", LogEvent.Type.CRITICAL);
      respondFailure(exchange);
    } 
  }
  
  /**
   * Method that moves the camera to the correct position.
   * @param ipcamera the camera to be moved
   * @param preset the preset used with the values for moving the camera.
   * @throws CameraConnectionException exception thrown when camera cannot connect.
   */
  public void movingCamera(IPCamera ipcamera, Preset preset) throws CameraConnectionException {
    Position position = preset.getPosition();
    ipcamera.moveTo(position, preset.getPanspeed(), preset.getTiltspeed());
    ipcamera.zoomTo(preset.getZoom());
    ipcamera.moveFocus(preset.getFocus());
    ipcamera.setAutoFocusOn(preset.isAutofocus());
    ipcamera.setIrisPosition(preset.getIris());
    ipcamera.setAutoIrisOn(preset.isAutoiris());
  
  }
}
