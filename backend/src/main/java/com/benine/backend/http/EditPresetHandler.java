package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
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
    
    Boolean overwritePreset = true;
    if (overwritePreset == true) {
      try {
        CreatePresetHandler.setPreset(ipcam, tagList);
      } catch (StreamNotAvailableException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      } catch (CameraConnectionException e) {
        e.printStackTrace();
      } catch (MalformedURIException e) {
        e.printStackTrace();
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
