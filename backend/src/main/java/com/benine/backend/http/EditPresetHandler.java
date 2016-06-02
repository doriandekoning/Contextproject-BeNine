package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.video.StreamNotAvailableException;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditPresetHandler extends RequestHandler {
  
  /**
   * Constructor for a new editPresetHandler that handles editing a preset.
   */
  public EditPresetHandler() {}
    
  private ServerController control;

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    
    String overwriteTag = request.getParameter("overwriteTag");
    String overwritePosition = request.getParameter("overwriteposition");
    
    control = ServerController.getInstance();
    String camID = request.getParameter("camera");
    IPCamera ipcam = (IPCamera)control.getCameraController().getCameraById(Integer.parseInt(camID));
        
    int presetID = Integer.parseInt(request.getParameter("presetid"));
    Preset preset = control.getPresetController().getPresetById(presetID);
   
    String tags = request.getParameter("tags");
    List<String> tagList = new ArrayList<String>();
    if (tags != null) {
      tagList = Arrays.asList(tags.split("\\s*,\\s*")); 
    }
    if (overwriteTag.equals("true")) {
      updateTag(preset, tagList);
    }
    
    if (overwritePosition.equals("true")) {
      try {
        updatePosition(ipcam,tagList,presetID);
      } catch (MalformedURIException | SQLException | StreamNotAvailableException e) {
        getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
        respondFailure(request,res);
      } catch (CameraConnectionException e) {
        getLogger().log("Cannot connect to camera.", LogEvent.Type.CRITICAL);
        respondFailure(request,res);
      } 
      
    }
    
    respondSuccess(request, res);
    request.setHandled(true);
    
  
  }
  
  /**
   * Updating the tag only.
   * @param preset the preset to be changed
   * @param tagList the tag to be added
   */
  public void updateTag(Preset preset, List<String> tagList) {
    preset.removeTags();
    preset.addTags(tagList);
  }
  
  /**
   * 
   * @param ipcam                         The camera object. 
   * @param tagList                       The tags belonging to the preset.
   * @param presetID                      The id of the preset. 
   * @throws IOException                  If the image cannot be created.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws SQLException                 If the preset cannot be written to the database.
   * @throws CameraConnectionException    If the camera cannot be reached.
   * @throws MalformedURIException        If there is an error in the request.
   */
  public void updatePosition(IPCamera ipcam, List<String> tagList, int presetID) throws 
  IOException, StreamNotAvailableException, SQLException, CameraConnectionException, 
  MalformedURIException {
    Preset newPreset = CreatePresetHandler.setPreset(ipcam, tagList);
    newPreset.setId(presetID);
    control.getDatabase().updatePreset(newPreset);
  }
  
  
}
