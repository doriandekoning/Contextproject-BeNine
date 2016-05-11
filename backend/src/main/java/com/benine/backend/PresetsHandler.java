package com.benine.backend;

import com.benine.backend.camera.Camera;
import com.benine.backend.database.DatabasePreset;

/**
 * Created by Ege on 11-5-2016.
 */
public class PresetsHandler {

  /**
   * Get all the presets from the database and set them to the right camera.
   */
  public void resetCameraPresets() {
    for(Camera camera : Main.getCameraController().getCameras()) {
      camera.setPresetsFromArrayList(Main.getDatabase().getAllPresetsCamera(camera.getId()));
    }
  }

  /**
   * Reset the database with the new presets.
   */
  public void resetPresetsInDatabase() {
    Main.getDatabase().resetDatabase();
    for(Camera camera : Main.getCameraController().getCameras()) {
      //Main.getDatabase().addCamera(camera.getId(), camera.getIP, camera.getName());
      int i = 0;
      for(DatabasePreset preset : camera.getPresets()) {
        Main.getDatabase().addPreset(camera.getId(), i, preset);
        i++;
      }
    }
  }

  /**
   * Add a preset to the first free position.
   * @param cameraId The id of the camera
   * @param preset The preset
   * @return The position the preset is added
   */
  public int addPreset(int cameraId, DatabasePreset preset) {
    DatabasePreset[] presets = Main.getCameraController().getCameraById(cameraId).getPresets();
    for (int i = 0; i < presets.length; i++) {
      if(presets[i] == null) {
        presets[i] = preset;
        return i;
      }
    }
    return -1;
  }

  /**
   * Add a preset to a specific position on the camera.
   * @param cameraId The id of the camera
   * @param preset The preset
   * @param position The position to add the preset
   */
  public void addPresetAtPosition(int cameraId, DatabasePreset preset, int position) {
    DatabasePreset[] presets = Main.getCameraController().getCameraById(cameraId).getPresets();
    presets[position] = preset;
  }

  /**
   * Get a preset from a camera with the presetId.
   * @param cameraId The id of the camera
   * @param presetId The preset id of the camera
   * @return The preset
   */
  public DatabasePreset getPreset(int cameraId, int presetId) {
    return Main.getCameraController().getCameraById(cameraId).getPresets()[presetId];
  }
}
