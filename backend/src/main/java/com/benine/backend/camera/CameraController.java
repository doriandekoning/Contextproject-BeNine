package com.benine.backend.camera;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;

import com.benine.backend.ServerController;
import com.benine.backend.video.StreamController;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to control the camera's.
 * Created on 4-5-16.
 */
public class CameraController {

  private ArrayList<Camera> cameras = new ArrayList<>();

  private int highestIdInUse = 1;
  
  private CameraFactoryProducer camFactoryProducer;
  
  private Config config;
  
  private Logger logger;
  
  private StreamController streamController;
  
  /**
   * Constructor of the camera controller which creates a camera factory producer.
   */
  public CameraController() {
    ServerController serverController = ServerController.getInstance();
    config = serverController.getConfig();
    logger = serverController.getLogger();
    streamController = serverController.getStreamController();
    camFactoryProducer = new CameraFactoryProducer(this);
  }

  /**
   * Adds a new camera to control.
   * @param camera the camera to add to this controller.
   */
  public void addCamera(Camera camera) {
    camera.setId(highestIdInUse);
    highestIdInUse++;
    cameras.add(camera);

    streamController.addCamera(camera);
  }

  /**
   * Loads all the camera's specified in the config in the camera controller.
   */
  public void loadConfigCameras() {
    int i = 1;
    String type =  config.getValue("camera_" + i + "_type");   
    while (type != null) {
      try {
        addCamera(camFactoryProducer.getFactory(type).createCamera(i));
      } catch (InvalidCameraTypeException e) {
        getLogger().log("Camera: " + i + " from the config can not be created.",
                                                                LogEvent.Type.WARNING);
      }
      i++;
      type = config.getValue("camera_" + i + "_type");
    }  
  }

  /**
   * Returns the singleton instance of the logger.
   * @return logger object.
   */
  public Logger getLogger() {
    return logger;
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
    return json.toJSONString();
  }
  
  /**
   * Get the JSON representation of this camera.
   * If the connection fails return an empty camera.
   * @param camera object to create a JSON of.
   * @return String representation of the JSON.
   */
  private JSONObject getCameraJSON(Camera camera) {
    try {
      return camera.toJSON();
    } catch (CameraConnectionException e) {
      getLogger().log("Failed to get the JSON representation of camera: " 
          + camera.getId(), LogEvent.Type.CRITICAL);
      JSONObject json = new JSONObject();
      json.put("id", camera.getId());
      json.put("unavailable", true);
      return json;
    }
  }

  /**
   * Returns a list of the cameras which are in use.
   * @return a list of the cameras in use
   */
  public List<Camera> camerasInUse() {
    ArrayList<Camera> list = new ArrayList<Camera>();
    for (Camera camera : getCameras()) {
      if (camera.isInUse()) {
        list.add(camera);
      }
    }
    return list;
  }

  public Config getConfig() {
    return config;
  }
}
