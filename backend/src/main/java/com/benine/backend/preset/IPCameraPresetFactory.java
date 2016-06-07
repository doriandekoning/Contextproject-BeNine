package com.benine.backend.preset;

import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;
import com.benine.backend.video.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import javax.imageio.ImageIO;

/**
 * Class used to create presets.
 */
public class IPCameraPresetFactory {

  /**
   * Creates a new preset based on the parameters supplied.
   * @param pos       The position of this preset.
   * @param focus     The focus of the prest
   * @param iris      The iris of the preset
   * @param autofocus The autofocus of the preset
   * @param panspeed  The panspeed of the preset
   * @param autoiris  The autoiris of the preset
   * @param tiltspeed The tiltspeed of the preset
   * @param cameraId  The id of the camera associated with this preset.
   * @return the created preset.
   */
  public IPCameraPreset createPreset(ZoomPosition pos, int focus, int iris,
                             boolean autofocus, int panspeed, int tiltspeed, boolean autoiris, int cameraId) {
    IPCameraPreset preset = new IPCameraPreset(cameraId);
    preset.setPosition(pos);
    preset.setZoom(pos.getZoom());
    preset.setFocus(focus);
    preset.setIris(iris);
    preset.setAutofocus(autofocus);
    preset.setPanspeed(panspeed);
    preset.setTiltspeed(tiltspeed);
    preset.setAutoiris(autoiris);
    preset.setCameraId(cameraId);
    return preset;
  }

  /**
   * Creates a new preset based on the parameters supplied.
   * @param pos       The position of this preset.
   * @param focus     The focus of the prest
   * @param iris      The iris of the preset
   * @param autofocus The autofocus of the preset
   * @param autoiris  The autoiris of the preset
   * @param tiltspeed The tiltspeed of the preset
   * @param panspeed  The panspeed of the preset
   * @param cameraId  The id of the camera associated with this preset.
   * @param tags      The tags for the preset
   * @return the created preset
   */
  public IPCameraPreset createPreset(ZoomPosition pos, int focus, int iris,
                             boolean autofocus, int panspeed, int tiltspeed,
                             boolean autoiris, int cameraId, Collection<String> tags) {
    IPCameraPreset preset = createPreset(pos, focus, iris,
            autofocus, panspeed, tiltspeed, autoiris, cameraId);
    preset.addTags(tags);
    return preset;
  }

  /**
   * Creates a preset using the current camera parameters.
   * @param cam IPCamera to create the preset of
   * @param panSpeed the panspeed for the preset
   * @param tiltSpeed the tiltspeed of the preset
   * @return the created preset.
   * @throws CameraConnectionException when camera cannot be reached.
   * @throws com.benine.backend.camera.CameraBusyException if the camera is busy
   * @throws IOException if the preset image cannot be stored.
   * @throws StreamNotAvailableException if the camera stream cannot be reached.
   */
  public IPCameraPreset createPreset(IPCamera cam, int panSpeed, int tiltSpeed)
          throws CameraConnectionException, IOException, StreamNotAvailableException {
    IPCameraPreset preset = new IPCameraPreset(cam.getId());
    preset.setCameraId(cam.getId());
    preset.setPosition(cam.getPosition());
    preset.setZoom(cam.getZoomPosition());
    preset.setFocus(cam.getFocusPosition());
    preset.setIris(cam.getIrisPosition());
    preset.setPanspeed(panSpeed);
    preset.setTiltspeed(tiltSpeed);
    preset.setAutoiris(cam.isAutoIrisOn());
    preset.setAutofocus(cam.isAutoFocusOn());
    try {
      createImage(cam, preset);
    } catch (Exception e) {
      ServerController.getInstance().getLogger().log("Error creating preset image", e);
    }
    return preset;
  }



  /**
   * Creates an image for a preset.
   * @param cam      The camera to take the image from.
   * @param preset      The preset
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws IOException  If the image cannot be written.
   * @throws SQLException if the image can not be saved in the database.
   */
  private void createImage(IPCamera cam, Preset preset) throws
          StreamNotAvailableException, IOException, SQLException {

    StreamController streamController = ServerController.getInstance().getStreamController();
    PresetController presetController = ServerController.getInstance().getPresetController();


    MJPEGStreamReader streamReader = (MJPEGStreamReader)
            streamController.getStreamReader(cam.getId());
    File path = new File("static" + File.separator + "presets" + File.separator
            + cam.getId() + "_" + preset.getId() + ".jpg");

    VideoFrame snapShot = streamReader.getSnapShot();
    MJPEGFrameResizer resizer = new MJPEGFrameResizer(160, 90);
    snapShot = resizer.resize(snapShot);

    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(snapShot.getImage()));
    ImageIO.write(bufferedImage, "jpg", path);


    preset.setImage(cam.getId() + "_" + preset.getId() + ".jpg");
    presetController.updatePreset(preset);
  }

}
