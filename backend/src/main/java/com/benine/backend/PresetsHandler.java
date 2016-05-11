package com.benine.backend;

import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.DatabasePreset;

/**
 * Created by ikben_000 on 11-5-2016.
 */
public class PresetsHandler {

//  CameraController controller;
//
//  public PresetsHandler(CameraController cameraController) {
//    controller = cameraController;
//  }

  public void resetCameraPresets() {
    for(Camera camera : Main.getCameraController().getCameras()) {
      camera.setPresetsFromArrayList(Main.getDatabase().getAllPresetsCamera(camera.getId()));
    }
  }

  public void resetPresetsInDatabase() {
    Main.getDatabase().resetDatabase();
    for(Camera camera : Main.getCameraController().getCameras()) {
      Main.getDatabase().addCamera(camera.getId(), camera.getIP, camera.getName());
      int i = 0;
      for(DatabasePreset preset : camera.getPresets()) {
        Main.getDatabase().addPreset(camera.getId(), i, preset);
        i++;
      }
    }
  }

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

  public void addPresetAtPosition(int cameraId, DatabasePreset preset, int position) {
    DatabasePreset[] presets = Main.getCameraController().getCameraById(cameraId).getPresets();
    presets[position] = preset;
  }
}
