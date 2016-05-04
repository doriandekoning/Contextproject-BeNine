package com.benine.backend.camera;

import com.benine.backend.LogWriter;
import com.benine.backend.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraController {

  private ArrayList<Camera> cameras = new ArrayList<>();

  public static Logger logger = setupLogger();

  private int highestIdInUse = 0;



  /**
   * Adds a new camera to control.
   * @param c
   */
  public void addCamera(Camera c) {
    highestIdInUse++;
    c.setId(highestIdInUse);
    cameras.add(c);
  }


  /**
   *
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
   * @return
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
    for(Camera c : cameras) {
      if(c.getId() == id) {
        return c;
      }
    }
    return null;
  }
}
