package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.PresetCamera;
import com.benine.backend.preset.Preset;
import com.benine.backend.video.MJPEGFrameResizer;
import com.benine.backend.video.MJPEGStreamReader;
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



public class CreatePresetHandler extends RequestHandler {

  /**
   * Constructs a create preset handler.
   * @param httpserver this handler belongs to.
   */
  public CreatePresetHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    try {
      String camID = request.getParameter("camera");
      if (camID == null) {
        throw new MalformedURIException("No Camera ID Specified.");
      }
      
      Camera camera = getCameraController().getCameraById(Integer.parseInt(camID));
      String tags = request.getParameter("tags");

      Set<String> tagList = new HashSet<>();
      if (tags != null) {
        tagList = new HashSet<>(Arrays.asList(tags.split("\\s*,\\s*"))); 
      } 
      getLogger().log(tagList.toString(), LogEvent.Type.CRITICAL);
      if (camera instanceof PresetCamera) {
        PresetCamera presetCamera = (PresetCamera) camera;
        Preset preset = presetCamera.createPreset(tagList);

        int presetID = getPresetController().addPreset(preset);
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
    } catch (CameraBusyException e) {
      getLogger().log("Camera is busy.", LogEvent.Type.WARNING);
      respondFailure(request, res);
    } finally {
      request.setHandled(true);
    }
  }

  /**
   * Creates an image for a preset.
   * @param cameraID      The id of the camera to take the image from.
   * @param presetID      The id of the preset used for naming.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws IOException  If the image cannot be written.
   * @throws SQLException if the image can not be saved in the database.
   */
  private void createImage(int cameraID, int presetID) throws
          StreamNotAvailableException, IOException, SQLException {

    MJPEGStreamReader streamReader = (MJPEGStreamReader)
                                            getStreamController().getStreamReader(cameraID);
    File path = new File("static" + File.separator + "presets" + File.separator
        + cameraID + "_" + presetID + ".jpg");

    VideoFrame snapShot = streamReader.getSnapShot();
    MJPEGFrameResizer resizer = new MJPEGFrameResizer(160, 90);
    snapShot = resizer.resize(snapShot);

    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(snapShot.getImage()));
    ImageIO.write(bufferedImage, "jpg", path);

    Preset preset = getPresetController().getPresetById(presetID);

    preset.setImage(cameraID + "_" + presetID + ".jpg");
    getPresetController().updatePreset(preset);
  }
}
