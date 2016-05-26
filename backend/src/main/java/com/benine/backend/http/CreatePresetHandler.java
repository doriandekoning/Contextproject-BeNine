package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
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
      String tags= request.getParameter("tags");
      if (tags == null) {
        throw new MalformedURIException("No tag specified.");
      } 
      
      List<String> tagList = Arrays.asList(tags.split("\\s*,\\s*"));
      
      
      if (camera instanceof IPCamera) {
        IPCamera ipcam = (IPCamera) camera;
        setPreset(ipcam, tagList);
        respondSuccess(request, res);
      } else {
        throw new MalformedURIException("Camera does not support presets or is nonexistent.");
      }

    } catch (MalformedURIException | StreamNotAvailableException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (SQLException e) {
      getLogger().log("Cannot connect to database ", LogEvent.Type.CRITICAL);
      respondFailure(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera.", LogEvent.Type.CRITICAL);
      respondFailure(request, res);
    }

    request.setHandled(true);
  }

  /**
   * Creates an image for a preset.
   * @param preset        A Preset object.
   * @param cameraID      The id of the camera to take the image from.
   * @param presetID      The id of the preset used for naming.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws IOException  If the image cannot be written.
   */
  private void createImage(Preset preset, int cameraID, int presetID) throws
          StreamNotAvailableException, IOException {
    StreamController streamController = ServerController.getInstance().getStreamController();

    StreamReader streamReader = streamController.getStreamReader(cameraID);
    BufferedImage bufferedImage = streamReader.getSnapShot();

    File path = new File("static" + File.separator + "presets" + File.separator
            + cameraID + "_" + presetID + ".jpg");

    ImageIO.write(bufferedImage, "jpg", path);
    preset.setImage(File.separator + path.toString());
  }

  /**
   * Sets a preset.
   * @param camera                        A Camera object.
   * @param tagList                           The tag belonging to the preset. 
   * @throws IOException                  If the image cannot be created.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws SQLException                 If the preset cannot be written to the database.
   * @throws CameraConnectionException    If the camera cannot be reached.
   * @throws MalformedURIException        If there is an error in the request.
   */
  private void setPreset(IPCamera camera, List<String> tagList)
          throws IOException, StreamNotAvailableException, SQLException,
          CameraConnectionException, MalformedURIException {
    PresetController presetController = ServerController.getInstance().getPresetController();

    Preset preset = createPreset(camera, tagList);
    createImage(preset, camera.getId(), preset.getId());
    presetController.addPreset(preset);
  }

  /**
   * Creates a preset from a camera.
   * @param camera    The camera to create the preset from.
   * @param tagList   The tag belonging to the preset. 
   * @return          A Preset object.
   * @throws CameraConnectionException If the camera cannot be reached.
   */
  private Preset createPreset(IPCamera camera, List<String> tagList) 
      throws CameraConnectionException {
    int zoom = camera.getZoomPosition();
    double pan = camera.getPosition().getPan();
    double tilt = camera.getPosition().getTilt();
    int focus = camera.getFocusPosition();
    int iris = camera.getIrisPosition();
    int panspeed = 15;
    int tiltspeed = 1;
    boolean autoiris = camera.isAutoIrisOn();
    boolean autofocus = camera.isAutoFocusOn();
    int cameraId = camera.getId();

    return new Preset(new Position(pan, tilt), zoom, focus, iris, autofocus, panspeed,
            tiltspeed, autoiris, cameraId, tagList);
  }
}
