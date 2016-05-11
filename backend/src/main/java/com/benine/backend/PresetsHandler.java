package com.benine.backend;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.util.ArrayList;

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
      if(camera instanceof IPCamera){
        IPCamera ipcamera = (IPCamera) camera;
        Main.getDatabase().addCamera(camera.getId(), ipcamera.getIpaddress());
        int i = 0;
        for(Preset preset : camera.getPresets()) {
          Main.getDatabase().addPreset(camera.getId(), i, preset);
          i++;
        }
      }
    }
  }

  /**
   * Add a preset to the first free position.
   * @param cameraId The id of the camera
   * @param preset The preset
   * @return The position the preset is added
   */
  public int addPreset(int cameraId, Preset preset) {
    Preset[] cameraPresets = Main.getCameraController().getCameraById(cameraId).getPresets();
    for (int i = 0; i < cameraPresets.length; i++) {
      if(cameraPresets[i] == null) {
        cameraPresets[i] = preset;
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
  public void addPresetAtPosition(int cameraId, Preset preset, int position) {
    Preset[] cameraPresets = Main.getCameraController().getCameraById(cameraId).getPresets();
    cameraPresets[position] = preset;
  }

  /**
   * Get a preset from a camera with the presetId.
   * @param cameraId The id of the camera
   * @param presetId The preset id of the camera
   * @return The preset
   */
  public Preset getPreset(int cameraId, int presetId) {
    return Main.getCameraController().getCameraById(cameraId).getPresets()[presetId];
  }
}
