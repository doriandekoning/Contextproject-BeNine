package com.benine.backend.camera;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.LogWriter;
import com.benine.backend.Logger;
import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.database.Database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class to control the camera's.
 * Created on 4-5-16.
 */
public class CameraController {

  private ArrayList<Camera> cameras = new ArrayList<>();

  public static final Logger logger = setupLogger();

  private int highestIdInUse = 1;
  
  private CameraFactoryProducer camFactoryProducer;
  
  /**
   * Constructor of the camera controller which creates a camera factory producer.
   */
  public CameraController() {
    camFactoryProducer = new CameraFactoryProducer();
  }

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
   * Loads all the camera's specified in the config in the camera controller.
   */
  public void loadConfigCameras() {
    Config config = ServerController.getInstance().getConfig();
    int i = 1;
    String type =  config.getValue("camera_" + i + "_type");   
    while (type != null) {
      try {
        addCamera(camFactoryProducer.getFactory(type).createCamera(i));
      } catch (InvalidCameraTypeException e) {
        logger.log("Camera: " + i + " from the config can not be created.", LogEvent.Type.WARNING);
        e.printStackTrace();
      }
      i++;
      type = config.getValue("camera_" + i + "_type");
    }  
  }
  
  /**
   * Returns the database
   * @return database to retrieve information from.
   */
  private Database getDatabase() {
    return ServerController.getInstance().getDatabase();
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
   * @throws SQLException No right connection found
   */
  public void getPresetsFromDatabase() throws SQLException {
    for (Camera camera : this.getCameras()) {
      camera.setPresetsFromArrayList(getDatabase()
                                      .getAllPresetsCamera(camera.getId()));
    }
  }

  /**
   * Reset the database with the new presets.
   * @throws SQLException No right connection found
   */
  public void resetPresetsInDatabase() throws SQLException {
    getDatabase().resetDatabase();
    for (Camera camera : this.getCameras()) {
      if (camera instanceof IPCamera) {
        IPCamera ipcamera = (IPCamera) camera;
        getDatabase().addCamera(camera.getId(), ipcamera.getIpaddress());
        for (int i = 0; i < camera.getPresets().length; i++) {
          if (camera.getPresets()[i] != null) {
            getDatabase().addPreset(camera.getId(), i, camera.getPresets()[i]);
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
   * @throws SQLException No right connection found
   */
  public int addPreset(int cameraId, Preset preset) throws SQLException {
    Preset[] cameraPresets = this.getCameraById(cameraId).getPresets();
    for (int i = 0; i < cameraPresets.length; i++) {
      if (cameraPresets[i] == null) {
        cameraPresets[i] = preset;
        cameraPresets[i].setId(i);
        getDatabase().addPreset(cameraId, i, preset);
        this.getCameraById(cameraId).setPresets(cameraPresets);
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
   * @throws SQLException No right connection found
   */
  public void addPresetAtPosition(int cameraId, Preset preset, int position) throws SQLException {
    Preset[] cameraPresets = this.getCameraById(cameraId).getPresets();
    if (cameraPresets[position] != null) {
      getDatabase().updatePreset(cameraId, position, preset);
    } else {
      getDatabase().addPreset(cameraId, position, preset);
    }
    cameraPresets[position] = preset;
  }

  /**
   * Get a preset from a camera with the presetId.
   * @param cameraId The id of the camera
   * @param presetPosition The preset id of the camera
   * @return The preset
   */
  public Preset getPreset(int cameraId, int presetPosition) {
    return getCameraById(cameraId).getPresets()[presetPosition];
  }

  /**
   * Deletes all presets from the cameras.
   */
  public void resetPresets() {
    for (Camera camera : this.getCameras()) {
      camera.setPresets(new Preset[16]);
    }
  }

  /**
   * Returns a json string of all the cameras.
   * @return json string of all the cameras.
   * @throws CameraConnectionException if a connection to a camera cannot be made.
   */
  public String getCamerasJSON() {
    // Create expected json object
    JSONObject json = new JSONObject();
    JSONArray array = new JSONArray();
    for (Camera camera : getCameras()) {
      array.add(getCameraJSON(camera));
    }
    json.put("cameras", array);
    return json.toString();
  }
  
  /**
   * Get the JSON representation of this camera.
   * If the connection fails return an empty camera.
   * @param camera object to create a JSON of.
   * @return String representation of the JSON.
   */
  private String getCameraJSON(Camera camera) {
    try {
      return camera.toJSON();
    } catch (CameraConnectionException e) {
      JSONObject json = new JSONObject();
      json.put("id", Integer.valueOf(camera.getId()));
      return json.toString();
    }
  }
}
