package com.benine.backend.preset.autopresetcreation;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;
import com.benine.backend.video.StreamNotAvailableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;


/**
 * Abstract class used for autocreating presets.
 */
public abstract class AutoPresetCreator {

  private static long timeout = 2000;

  /**
   * Automatically creates presets for the selected camera.
   * Calll generatePositions to get the positions of the presets.
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
          TimeoutException, IOException, StreamNotAvailableException {
    if (cam.isBusy()) {
      throw new CameraBusyException("The camera is busy.", cam.getId());
    }
    cam.setBusy(true);
    ArrayList<Preset> presets = new ArrayList<>();
    for (ZoomPosition pos : generatePositions(cam, subViews)) {
      cam.setBusy(false);
      cam.moveTo(pos, 2, 30);
      cam.waitUntilAtPosition(pos, timeout);
      cam.setBusy(true);
      presets.add(new IPCameraPreset(cam, 2, 30));
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
