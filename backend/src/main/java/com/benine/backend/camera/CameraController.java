package com.benine.backend.camera;

import com.benine.backend.LogWriter;
import com.benine.backend.Logger;
import com.benine.backend.Main;
import com.benine.backend.Preset;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraController {

  private ArrayList<Camera> cameras = new ArrayList<>();

  public static final Logger logger = setupLogger();

  private int highestIdInUse = 0;



  /**
   * Adds a new camera to control.
   * @param camera the camera to add to this controller.
   */
  public void addCamera(Camera camera) {
    camera.setId(highestIdInUse);
    highestIdInUse++;
    cameras.add(camera);
  }


  /**
   * Sets up the logger.
   * @return logger object.
   */
  private static Logger setupLogger() {
    // Setup logger
    try {
      return new Logger(new LogWriter("logs" + File.separator + "mainlog"));
    } catch (IOException e) {
      System.out.println("Cannot create log file");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns the camera list this controller controls.
   * @return Arraylist of Camera objects.
   */
  public ArrayList<Camera> getCameras() {
    return cameras;
  }

  /**
   * Finds camera with specified id.
   * @param id the id of the camera
   * @return the camera associated with the specified id or null if it does not exist.
   */
  public Camera getCameraById(int id) {
    for (Camera c : cameras) {
      if (c.getId() == id) {
        return c;
      }
    }
    return null;
  }

  /**
   * Get all the presets from the database and set them to the right camera.
   */
  public void getPresetsFromCamera() {
    for(Camera camera : this.getCameras()) {
      camera.setPresetsFromArrayList(Main.getDatabase().getAllPresetsCamera(camera.getId()));
    }
  }

  /**
   * Reset the database with the new presets.
   */
  public void resetPresetsInDatabase() {
    Main.getDatabase().resetDatabase();
    for(Camera camera : this.getCameras()) {
      if(camera instanceof IPCamera){
        IPCamera ipcamera = (IPCamera) camera;
        Main.getDatabase().addCamera(camera.getId(), ipcamera.getIpaddress());
        for(int i = 0; i < camera.getPresets().length; i++) {
          if(camera.getPresets()[i] != null) {
            Main.getDatabase().addPreset(camera.getId(), i, camera.getPresets()[i]);
          }
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
    Preset[] cameraPresets = this.getCameraById(cameraId).getPresets();
    for (int i = 0; i < cameraPresets.length; i++) {
      if(cameraPresets[i] == null) {
        cameraPresets[i] = preset;
        Main.getDatabase().addPreset(cameraId, i, preset);
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
    Preset[] cameraPresets = this.getCameraById(cameraId).getPresets();
    if(cameraPresets[position] != null) {
      Main.getDatabase().updatePreset(cameraId, position, preset);
    }
    else {
      Main.getDatabase().addPreset(cameraId, position, preset);
    }
    cameraPresets[position] = preset;
  }

  /**
   * Get a preset from a camera with the presetId.
   * @param cameraId The id of the camera
   * @param presetId The preset id of the camera
   * @return The preset
   */
  public Preset getPreset(int cameraId, int presetId) {
    return getCameraById(cameraId).getPresets()[presetId];
  }

  /**
   * Deletes all presets from the cameras.
   */
  public void resetPresets() {
    for(Camera camera : this.getCameras()) {
      camera.setPresets(new Preset[16]);
    }
  }
}
