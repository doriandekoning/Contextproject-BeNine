package com.benine.backend.http;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.video.StreamNotAvailableException;


/**
 * Class that handles editing presets.
 */
public class EditPresetHandler extends RequestHandler {
  
  /**
   * Constructor for a new editPresetHandler that handles editing a preset.
   */
  public EditPresetHandler() {}
    
  private ServerController control = ServerController.getInstance();

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    
    try {
      String overwriteTag = request.getParameter("overwritetag");
      String overwritePosition = request.getParameter("overwriteposition");
      int presetID = Integer.parseInt(request.getParameter("presetid"));
      String tags = request.getParameter("tags");
      
      Preset preset = control.getPresetController().getPresetById(presetID);
     
      List<String> tagList = new ArrayList<String>();
      if (tags != null) {
        tagList = Arrays.asList(tags.split("\\s*,\\s*")); 
      }
      if (overwriteTag.equals("true")) {
        updateTag(preset, tagList);
      }
      
      if (overwritePosition.equals("true")) {
        updatePosition(preset.getId(),tagList,presetID);
      }
    } catch (MalformedURIException | SQLException | StreamNotAvailableException e) {
      getLogger().log(e.getMessage(), e);
      respondFailure(request,res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera.", e);
      respondFailure(request,res);
    } 
        
    respondSuccess(request, res);
    request.setHandled(true);  
  }
  
  /**
   * Updating the tag only.
   * @param preset the preset to be changed
   * @param tagList the tag to be added
   * @return the tag added to the preset
   */
  public Set<String> updateTag(Preset preset, List<String> tagList) {
    preset.removeTags();
    preset.addTags(tagList);
    return preset.getTags();
  }
  
  /**
   * Editing an already existing preset by removing the old preset and creating a new 
   * preset with the same preset and camera id. It also creates a new image that belongs to the 
   * preset and updates the database.
   * @param camID                         The id of the camera object. 
   * @param tagList                       The tags belonging to the preset.
   * @param presetID                      The id of the preset. 
   * @throws IOException                  If the image cannot be created.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws SQLException                 If the preset cannot be written to the database.
   * @throws CameraConnectionException    If the camera cannot be reached.
   * @throws MalformedURIException        If there is an error in the request.
   */
  public void updatePosition(int camID, List<String> tagList, int presetID) throws 
  IOException, StreamNotAvailableException, SQLException, CameraConnectionException, 
  MalformedURIException {
    IPCamera ipcam = (IPCamera)control.getCameraController()
        .getCameraById(camID);   
    CreatePresetHandler handler = new CreatePresetHandler();
    Preset newPreset = handler.setPreset(ipcam, tagList);
    newPreset.setId(presetID);
    control.getDatabase().updatePreset(newPreset);
  }
  
  
}
