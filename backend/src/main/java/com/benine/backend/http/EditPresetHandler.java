package com.benine.backend.http;

import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;
import com.benine.backend.video.MJPEGFrameResizer;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.StreamController;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.VideoFrame;

import org.eclipse.jetty.server.Request;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
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
     
      Set<String> tagList = new HashSet<>();
      if (tags != null) {
        tagList = new HashSet<>(Arrays.asList(tags.split("\\s*,\\s*"))); 
      }
      if (overwriteTag.equals("true")) {
        updateTag(preset, tagList);
      }
      
      if (overwritePosition.equals("true")) {
        updatePosition(preset);
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
   */
  private void updateTag(Preset preset, Set<String> tagList) {
    preset.removeTags();
    preset.addTags(tagList);
  }
  
  /**
   * Editing an already existing preset by removing the old preset and creating a new 
   * preset with the same preset and camera id. It also creates a new image that belongs to the 
   * preset and updates the database.
   * @param preset                        The preset to be updated.
   * @throws IOException                  If the image cannot be created.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws SQLException                 If the preset cannot be written to the database.
   * @throws CameraConnectionException    If the camera cannot be reached.
   * @throws MalformedURIException        If there is an error in the request.
   */
  private void updatePosition(Preset preset) throws 
  IOException, StreamNotAvailableException, SQLException, CameraConnectionException, 
  MalformedURIException {
    IPCamera ipcam = (IPCamera)control.getCameraController().getCameraById(preset.getCameraId());   
    PresetController presetController = ServerController.getInstance().getPresetController();
    Preset newPreset = ipcam.createPreset(preset.getTags());
    newPreset.setId(preset.getId());
   
    createImage(preset.getCameraId(), newPreset.getId());
    presetController.updatePreset(newPreset);
  }
  
  /**
   * Creates an image for a preset.
   * @param cameraID      The id of the camera to take the image from.
   * @param presetID      The id of the preset used for naming.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws IOException  If the image cannot be written.
   * @throws SQLException when preset can't be updated
   */
  private void createImage(int cameraID, int presetID) throws
          StreamNotAvailableException, IOException, SQLException {
    StreamController streamController = ServerController.getInstance().getStreamController();

    MJPEGStreamReader streamReader = (MJPEGStreamReader) streamController.getStreamReader(cameraID);
    VideoFrame snapShot = streamReader.getSnapShot();
    MJPEGFrameResizer resizer = new MJPEGFrameResizer(160, 90);
    snapShot = resizer.resize(snapShot);

    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(snapShot.getImage()));

    File path = new File("static" + File.separator + "presets" + File.separator
            + cameraID + "_" + presetID + ".jpg");

    ImageIO.write(bufferedImage, "jpg", path);
    
    PresetController presetController = ServerController.getInstance().getPresetController();
    Preset preset = presetController.getPresetById(presetID);
    preset.setImage(cameraID + "_" + presetID + ".jpg");
    presetController.updatePreset(preset);
  }  
}
