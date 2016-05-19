package com.benine.backend.camera;

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
 * Created by dorian on 4-5-16.
 */
public class CameraController {

  private ArrayList<Camera> cameras = new ArrayList<>();

  public static final Logger logger = setupLogger();

  private int highestIdInUse = 1;

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
   * Returns a json string of all the cameras.
   * @return json string of all the cameras.
   * @throws CameraConnectionException if a connection to a camera cannot be made.
   */
  public String getCamerasJSON() throws CameraConnectionException {
    // Create expected json object
    JSONObject json = new JSONObject();
    JSONArray array = new JSONArray();
    for (Camera camera : getCameras()) {
      array.add(camera.toJSON());
    }
    json.put("cameras", array);
    return json.toString();
  }
}
