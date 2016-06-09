package com.benine.backend.preset.autopresetcreation;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;
import com.benine.backend.video.StreamNotAvailableException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;


/**
 * Abstract class used for autocreating presets.
 */
public abstract class AutoPresetCreator {

  private static long timeout = 2000;
  private static PresetController presetController;

  public AutoPresetCreator(PresetController presetController) {
    this.presetController = presetController;
  }
  /**
   * Automatically creates presets for the selected camera.
   * Calls generatePositions to get the positions of the presets.
   * Automatically adds the presets to the presetcontroller.
   * @param cam the camera to create presets for.
   * @param subViews the subviews to generate positions for.
   * @return A collection of the created presets.
   * @throws CameraConnectionException when camera cannot be reached.
   * @throws InterruptedException when interupted while waiting for cam to move.
   * @throws TimeoutException if the camera is moving too slow or not at all.
   * @throws CameraBusyException if the camera is busy.
   * @throws IOException if exception occurs when creating the preset image.
   * @throws StreamNotAvailableException if a camera stream cannot be reached.
   */
  public Collection<Preset> createPresets(IPCamera cam, Collection<SubView> subViews)
          throws CameraConnectionException, CameraBusyException, InterruptedException,
          TimeoutException, IOException, StreamNotAvailableException, SQLException {
    if (cam.isBusy()) {
      throw new CameraBusyException("The camera is busy.", cam.getId());
    }
    cam.setBusy(true);
    ArrayList<Preset> presets = new ArrayList<>();

    cam.setBusy(false);
    cam.setAutoFocusOn(true);
    cam.setBusy(true);
    for (ZoomPosition pos : generatePositions(cam, subViews)) {
      cam.setBusy(false);
      Thread.sleep(200);
      cam.moveTo(pos, 2, 30);
      Thread.sleep(200);
      cam.zoomTo(pos.getZoom());
      cam.setBusy(true);
      cam.waitUntilAtPosition(pos, timeout);
      cam.setBusy(false);
      IPCameraPreset preset = new IPCameraPreset(pos, 0, 0, true, true, cam.getId());
      cam.setBusy(true);
      presetController.addPreset(preset);
      preset.createImage(cam);
      presets.add(preset);
    }
    cam.setBusy(false);
    return presets;
  }


  /**
   * Generates the positions to create pesets at.
   * @param cam the camera to create positions for.
   * @param subViews the subviews to generate positions for.
   * @return A collection of positions.
   * @throws CameraConnectionException when the camera cannot be reached.
   */
  protected abstract Collection<ZoomPosition> generatePositions(IPCamera cam,
                                                                Collection<SubView> subViews)
          throws CameraConnectionException;


  /**
   * Setter for timeout.
   * @param t the new timeout.
   */
  public static void setTimeout(long t) {
    timeout = t;
  }

}
