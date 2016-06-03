package com.benine.backend;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.video.StreamController;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.imageio.ImageIO;

/**
 * Class used to create presets.
 */
public class PresetFactory {

  /**
   * Creates a new preset based on the parameters supplied.
   * @param pos       The position of this preset.
   * @param zoom      The zoom of the preset
   * @param focus     The focus of the prest
   * @param iris      The iris of the preset
   * @param autofocus The autofocus of the preset
   * @param panspeed  The panspeed of the preset
   * @param autoiris  The autoiris of the preset
   * @param tiltspeed The tiltspeed of the preset
   * @param cameraId  The id of the camera associated with this preset.
   * @return the created preset.
   */
  public Preset createPreset(Position pos, int zoom, int focus, int iris,
                boolean autofocus, int panspeed, int tiltspeed, boolean autoiris, int cameraId) {
    Preset preset = new Preset();
    preset.setPosition(pos);
    preset.setZoom(zoom);
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
   * @param zoom      The zoom of the preset
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
  public Preset createPreset(Position pos, int zoom, int focus, int iris,
                             boolean autofocus, int panspeed, int tiltspeed,
                             boolean autoiris, int cameraId, Collection<String> tags) {
    Preset preset = createPreset(pos, zoom, focus, iris,
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
  public Preset createPreset(IPCamera cam, int panSpeed, int tiltSpeed)
          throws CameraConnectionException, IOException, StreamNotAvailableException {
    Preset preset = new Preset();
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
      createImage(cam, preset.getId());
    } catch (Exception e) {
      ServerController.getInstance().getLogger().log("Error creating preset image", e);
    }
    return preset;
  }

  /**
   * Creates an image for a preset.
   * @param cam      The camera to take the image from.
   * @param presetID      The id of the preset used for naming.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws IOException  If the image cannot be written.
   */
  private void createImage(IPCamera cam, int presetID) throws
          StreamNotAvailableException, IOException {
    StreamController streamController = ServerController.getInstance().getStreamController();

    StreamReader streamReader = streamController.getStreamReader(cam.getId());
    BufferedImage bufferedImage = streamReader.getSnapShot();

    File path = new File("static" + File.separator + "presets" + File.separator
            + cam.getId() + "_" + presetID + ".jpg");

    ImageIO.write(bufferedImage, "jpg", path);
    PresetController presetController = ServerController.getInstance().getPresetController();

    presetController.getPresetById(presetID).setImage(File.separator + path.toString());
  }

}
