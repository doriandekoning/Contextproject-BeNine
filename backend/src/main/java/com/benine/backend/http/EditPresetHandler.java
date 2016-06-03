package com.benine.backend.http;

import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.video.StreamController;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;

import org.eclipse.jetty.server.Request;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





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
        updatePosition(preset,presetID);
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
  private Set<String> updateTag(Preset preset, List<String> tagList) {
    preset.removeTags();
    preset.addTags(tagList);
    return preset.getTags();
  }
  
  /**
   * Editing an already existing preset by removing the old preset and creating a new 
   * preset with the same preset and camera id. It also creates a new image that belongs to the 
   * preset and updates the database.
   * @param presetID                      The id of the preset. 
   * @param preset                        The preset to be updated.
   * @throws IOException                  If the image cannot be created.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws SQLException                 If the preset cannot be written to the database.
   * @throws CameraConnectionException    If the camera cannot be reached.
   * @throws MalformedURIException        If there is an error in the request.
   */
  private void updatePosition(Preset preset, int presetID) throws 
  IOException, StreamNotAvailableException, SQLException, CameraConnectionException, 
  MalformedURIException {
    IPCamera ipcam = (IPCamera)control.getCameraController().getCameraById(preset.getCameraId());   
    PresetController presetController = ServerController.getInstance().getPresetController();
    
    updatePreset(preset, ipcam);
    
    presetController.addPreset(preset);
    createImage(preset.getCameraId(), presetID);
    control.getDatabase().updatePreset(preset);
  }
  
  /**
   * Creates an image for a preset.
   * @param cameraID      The id of the camera to take the image from.
   * @param presetID      The id of the preset used for naming.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws IOException  If the image cannot be written.
   */
  private void createImage(int cameraID, int presetID) throws
          StreamNotAvailableException, IOException {
    StreamController streamController = ServerController.getInstance().getStreamController();

    StreamReader streamReader = streamController.getStreamReader(cameraID);
    BufferedImage bufferedImage = streamReader.getSnapShot();

    File path = new File("static" + File.separator + "presets" + File.separator
            + cameraID + "_" + presetID + ".jpg");

    ImageIO.write(bufferedImage, "jpg", path);
    PresetController presetController = ServerController.getInstance().getPresetController();
    
    presetController.getPresetById(presetID).setImage(File.separator + path.toString());
  }
  
  /**
   * Updates a preset from a camera.
   * @param camera    The camera to create the preset from.
   * @param preset    The preset to be updated.
   * @throws CameraConnectionException If the camera cannot be reached.
   */
  private void updatePreset(Preset preset, IPCamera camera) 
      throws CameraConnectionException {
    
    preset.setZoom(camera.getZoomPosition());
    preset.setPosition(new Position(camera.getPosition().getPan(), camera.getPosition().getTilt()));
    preset.setFocus(camera.getFocusPosition());
    preset.setIris(camera.getIrisPosition());
    preset.setAutofocus(camera.isAutoFocusOn());
    preset.setAutoiris(camera.isAutoIrisOn());
    
  }
  
  
}
