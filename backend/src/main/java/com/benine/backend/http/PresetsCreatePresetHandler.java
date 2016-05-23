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

public class PresetsCreatePresetHandler extends RequestHandler {

  /**
   * Constructor for a new PresetsCreatePresetHandler, handling the /presets/createpreset request.
   */
  public PresetsCreatePresetHandler() {};

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
      System.out.println(ServerController.getInstance().getCameraController().getCameraById(1));

      if (camera instanceof IPCamera) {
        IPCamera ipcam = (IPCamera) camera;
        System.out.println(ipcam);
        setPreset(ipcam);
        respondSuccess(request, res);
      } else {
        throw new MalformedURIException("Camera does not support presets or is nonexistent.");
      }

    } catch (MalformedURIException | StreamNotAvailableException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (SQLException e) {
      respondFailure(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera.", LogEvent.Type.CRITICAL);
      respondFailure(request, res);
    }

    request.setHandled(true);
  }

  private void createImage(Preset preset, int cameraID, int presetID) throws
          StreamNotAvailableException, IOException {
    StreamController streamController = ServerController.getInstance().getStreamController();

    StreamReader streamReader = streamController.getStreamReader(cameraID);
    BufferedImage bufferedImage = streamReader.getSnapShot();

    File path = new File("static" + File.separator + "presets" + File.separator
            + cameraID + "_" + presetID + ".jpg");

    ImageIO.write(bufferedImage, "jpg", path);
    preset.setImage(path.toString());
  }

  private void setPreset(IPCamera camera)
          throws IOException, StreamNotAvailableException, SQLException,
          CameraConnectionException, MalformedURIException {
    PresetController presetController = ServerController.getInstance().getPresetController();

    Preset preset = createPreset(camera);
    createImage(preset, camera.getId(), preset.getId());
    presetController.addPreset(preset);
  }

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
