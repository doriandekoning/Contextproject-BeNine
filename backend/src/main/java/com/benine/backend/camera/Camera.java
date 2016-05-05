package com.benine.backend.camera;


/**
 * Interface for communication with remote camera's.
 * @author Bryan
 */
public interface Camera {

  /**
   * Method to create a json object describinding the camera.
   * @return Json object in the form of a string.
   * @throws CameraConnectionException When the information can not be retrieved.
   */
  String toJSON() throws CameraConnectionException;

  /**
   * Set the ID of this camera.
   * @param id to set.
   */
  void setId(int id);

  /**
   * Get the ID of this camera.
   * @return ID of this camra.
   */
  int getId();
}
