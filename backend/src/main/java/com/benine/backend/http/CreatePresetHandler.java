package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.PresetCamera;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;
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

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CreatePresetHandler extends RequestHandler {

  /**
   * Constructor for a new CreatePresetHandler, handling the /presets/createpreset request.
   */
  public CreatePresetHandler() {}

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    try {
      String camID = request.getParameter("camera");
      if (camID == null) {
        throw new MalformedURIException("No Camera ID Specified.");
      }
      
      CameraController cameraController = ServerController.getInstance().getCameraController();
      Camera camera = cameraController.getCameraById(Integer.parseInt(camID));
      String tags = request.getParameter("tags");
      List<String> tagList = null;
      
      if (tags != null) {
        tagList = Arrays.asList(tags.split("\\s*,\\s*")); 
      } else {
        tagList = new ArrayList<String>();
      }
      if (camera instanceof PresetCamera) {
        PresetCamera presetCamera = (PresetCamera) camera;
        Preset preset = presetCamera.createPreset(tagList);
        PresetController presetController = ServerController.getInstance().getPresetController();
        int presetID = presetController.addPreset(preset);
        createImage(camera.getId(), presetID);
        respondSuccess(request, res);
      } else {
        throw new MalformedURIException("Camera does not support presets or is nonexistent.");
      }
    } catch (MalformedURIException | StreamNotAvailableException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (SQLException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera.", LogEvent.Type.CRITICAL);
      respondFailure(request, res);
    }

    request.setHandled(true);
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
}
