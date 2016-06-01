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
 

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    
    Boolean overwriteTag = true;
        
    String camID = request.getParameter("camera");
    IPCamera ipcam = (IPCamera) ServerController.getInstance().getCameraController()
        .getCameraById(Integer.parseInt(camID));
        
    int presetID = Integer.parseInt(request.getParameter("presetid"));
    Preset preset = ServerController.getInstance().getPresetController().getPresetById(presetID);
   
    String tags = request.getParameter("tags");
    List<String> tagList = new ArrayList<String>();
    if (tags != null) {
      tagList = Arrays.asList(tags.split("\\s*,\\s*")); 
    }
    if (overwriteTag == true) {
      updateTag(preset, tagList);
    }
    
    Boolean overwritePreset = true;
    if (overwritePreset == true) {
      try {
        Preset newPreset = CreatePresetHandler.setPreset(ipcam, tagList);
        newPreset.setId(presetID);
        ServerController.getInstance().getDatabase().updatePreset(newPreset);
      } catch (MalformedURIException | SQLException | StreamNotAvailableException e) {
        getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
        respondFailure(request,res);
      } catch (CameraConnectionException e) {
        getLogger().log("Cannot connect to camera.", LogEvent.Type.CRITICAL);
        respondFailure(request,res);
      } 
      
    }
    
    preset.setId(presetID);
    
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
}
