package com.benine.backend.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;



public class EditPresetHandler extends RequestHandler {
  
  /**
   * Constructor for a new editPresetHandler that handles editing a preset.
   */
  public EditPresetHandler() {}
 

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    
    Boolean overwriteTag = true;
    Boolean overwritePreset = true;
    
    String camID = request.getParameter("camera");
    if (camID == null) {
      try {
        throw new MalformedURIException("No Camera ID Specified.");
      } catch (MalformedURIException e) {
        getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
        respondFailure(request, res);
      }
    }
    CameraController cameraController = ServerController.getInstance().getCameraController();
    IPCamera ipcam = (IPCamera) cameraController.getCameraById(Integer.parseInt(camID));
    
    String tags = request.getParameter("tags");
    List<String> tagList = new ArrayList<String>();
    if (tags != null) {
      tagList = Arrays.asList(tags.split("\\s*,\\s*")); 
    } 
    
    int presetID = Integer.parseInt(request.getParameter("presetid"));
    PresetController presetController = ServerController.getInstance().getPresetController();
    Preset preset = presetController.getPresetById(presetID);
    
    if (overwriteTag == true) {
      updateTag(preset, tagList);
    }
    
    if (overwritePreset == true) {
     // CreatePresetHandler.setPreset(ipcam, tagList);
      
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
}
