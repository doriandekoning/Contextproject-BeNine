package com.benine.backend.preset.autopresetcreation;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.FocusValue;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.camera.ipcameracontrol.IrisValue;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.PresetController;
import com.benine.backend.video.StreamNotAvailableException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;



/**
 * Abstract class used for auto-creating presets.
 */
public abstract class AutoPresetCreator {

  private static long timeout = 5000;
  private PresetController presetController;
  private int generatedPresets;
  private int totalAmountPresets;
  
  /**
   * Creates a new AutoPresetCreator object.
   * @param presetController the presetController to add the created presets too.
   */
  public AutoPresetCreator(PresetController presetController) {
    this.presetController = presetController;
  }

  /**
   * Automatically creates presets for the selected camera.
   * Calls generatePositions to get the positions of the presets.
   * Automatically adds the presets to the presetcontroller.
   * @param cam the camera to create presets for.
   * @param subViews the sub-views to generate positions for.
   * @return A collection of the ids of the created presets.
   * @throws CameraConnectionException when camera cannot be reached.
   * @throws InterruptedException when interrupted while waiting for cam to move.
   * @throws TimeoutException if the camera is moving too slow or not at all.
   * @throws CameraBusyException if the camera is busy.
   * @throws IOException if exception occurs when creating the preset image.
   * @throws StreamNotAvailableException if a camera stream cannot be reached.
   * @throws SQLException if exception occurs while writing to the database.
   */
  public Collection<IPCameraPreset> createPresets(IPCamera cam, Collection<SubView> subViews)
          throws CameraConnectionException, CameraBusyException, InterruptedException,
          TimeoutException, IOException, StreamNotAvailableException, SQLException {
    if (cam.isBusy()) {
      throw new CameraBusyException("The camera is busy.", cam.getId());
    }
    cam.setBusy(true);
    ArrayList<IPCameraPreset> presets = new ArrayList<>();
   
    cam.setBusy(false);
    cam.setAutoFocusOn(true);
    cam.setBusy(true);
    for (ZoomPosition pos : generatePositions(cam, subViews)) {
      cam.setInUse();
      IPCameraPreset currentPreset = generatePresetFromPos(pos,cam);
      presetController.addPreset(currentPreset);
      presets.add(currentPreset);
      generatedPresets++;
    }
    cam.setBusy(false);
    return presets;
  }


  /**
   * Creates a preset from a position for a given camera.
   * @param pos the position to create a preset for
   * @param cam the camera to create a preset for
   * @return the created preset
   * @throws InterruptedException when interrupted
   * @throws InterruptedException when interrupted while waiting for cam to move.
   * @throws CameraBusyException if the camera is busy.
   * @throws IOException if exception occurs when creating the preset image.
   * @throws StreamNotAvailableException if a camera stream cannot be reached.
   * @throws SQLException if exception occurs while writing to the database.
   * @throws CameraConnectionException if the camera cannot be reached.
   */
  public IPCameraPreset generatePresetFromPos(ZoomPosition pos, IPCamera cam)
          throws InterruptedException, CameraConnectionException,
          CameraBusyException, SQLException, IOException, StreamNotAvailableException {
    cam.setBusy(false);
    Thread.sleep(200);
    cam.moveTo(pos, 2, 30);
    Thread.sleep(200);
    cam.zoomTo(pos.getZoom());
    cam.setBusy(true);
    Thread.sleep(timeout);
    cam.setBusy(false);
    IPCameraPreset preset = new IPCameraPreset(pos, new FocusValue(0, true),
                                          new IrisValue(0, true), cam.getId());
    cam.setBusy(true);
    presetController.addPreset(preset);
    presetController.createImage(preset);
    return preset;
  }
  
  /**
   * Generates the positions to create presets at.
   * @param cam the camera to create positions for.
   * @param subViews the sub-views to generate positions for.
   * @return A collection of positions.
   * @throws CameraConnectionException when the camera cannot be reached.
   */
  protected abstract ArrayList<ZoomPosition> generatePositions(IPCamera cam,
                                                                Collection<SubView> subViews)
          throws CameraConnectionException;


  /**
   * Setter for timeout.
   * @param t the new timeout.
   */
  public static void setTimeout(long t) {
    timeout = t;
  }

  /**
   * Getter for amount subviews already created.
   * @return Amount of created presets.
   */
  public int getGeneratedPresetsAmount() {
    return generatedPresets;
  }


  /**
   * Returns total amount of presets this creator will create when the create method is run.
   * @return amount of created presets.
   */
  public abstract int getTotalAmountPresets();

}
