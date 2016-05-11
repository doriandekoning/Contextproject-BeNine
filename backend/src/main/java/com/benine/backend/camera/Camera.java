package com.benine.backend.camera;


import com.benine.backend.database.DatabasePreset;

import java.util.ArrayList;

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

  /**
   * Get the list of presets from this camera.
   * @return The preset array
   */
  DatabasePreset[] getPresets();

  /**
   * Set the list of presets from this camera.
   * @param presets The preset array
   */
  void setPresets(DatabasePreset[] presets);

  /**
   * Set the list of presets from this camera of an arraylist.
   * @param presets The ArrayList of presets
   */
  void setPresetsFromArrayList(ArrayList<DatabasePreset> presets);
}
