package com.benine.backend.camera;

import java.util.ArrayList;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraController {

  private ArrayList<Camera> cameras = new ArrayList<>();



  /**
   * Adds a new camera to control.
   * @param c
   */
  public void addCamera(Camera c) {
    cameras.add(c);
  }

  /**
   * Returns the camera list this controller controls.
   * @return
   */
  public ArrayList<Camera> getCameras() {
    return cameras;
  }
}
