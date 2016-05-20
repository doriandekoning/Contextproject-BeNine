package com.benine.backend.camera;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.LogWriter;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;
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
