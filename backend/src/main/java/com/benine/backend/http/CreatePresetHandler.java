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

      if (camera instanceof IPCamera) {
        IPCamera ipcam = (IPCamera) camera;
        setPreset(ipcam);
        respondSuccess(request, res);
      } else {
        throw new MalformedURIException("Camera does not support presets or is nonexistent.");
      }

    } catch (MalformedURIException | StreamNotAvailableException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (SQLException e) {
      e.printStackTrace();
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

  /**
   * Sets a preset.
   * @param camera                        A Camera object.
   * @throws IOException                  If the image cannot be created.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws SQLException                 If the preset cannot be written to the database.
   * @throws CameraConnectionException    If the camera cannot be reached.
   * @throws MalformedURIException        If there is an error in the request.
   */
  private void setPreset(IPCamera camera)
          throws IOException, StreamNotAvailableException, SQLException,
          CameraConnectionException, MalformedURIException {
    PresetController presetController = ServerController.getInstance().getPresetController();
    getLogger().log("test cameraID + " +camera.getId() , LogEvent.Type.WARNING);
    int presetID = presetController.addPreset(createPreset(camera));
    getLogger().log("test + " +presetID , LogEvent.Type.WARNING);
    createImage(camera.getId(), presetID);
  }

  /**
   * Creates a preset from a camera.
   * @param camera    The camera to create the preset from.
   * @return          A Preset object.
   * @throws CameraConnectionException If the camera cannot be reached.
   */
  private Preset createPreset(IPCamera camera) throws CameraConnectionException {
    int zoom = camera.getZoomPosition();
    int pan = (int) camera.getPosition().getPan();
    int tilt = (int) camera.getPosition().getTilt();
    int focus = camera.getFocusPosition();
    int iris = camera.getIrisPosition();
    int panspeed = 15;
    int tiltspeed = 1;
    boolean autoiris = camera.isAutoIrisOn();
    boolean autofocus = camera.isAutoFocusOn();
    // TODO get cameraId from db
    int cameraId = 0;

    Position position = new Position(pan, tilt);
    return new Preset(
            position, zoom, focus,
            iris, autofocus, panspeed,
            tiltspeed, autoiris, cameraId
    );
  }
}
