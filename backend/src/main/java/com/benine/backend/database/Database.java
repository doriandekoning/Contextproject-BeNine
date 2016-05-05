package com.benine.backend.database;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface for communication with the database.
 *
 * @author Ege
 */
public interface Database {

  /**
   * Checks if database is connected.
   *
   * @return true if database is connected
   * @throws SQLException No right connection found
   */
  boolean isConnected() throws SQLException;

  /**
   * Add a preset to the database.
   *
   * @param camera             ID of the camera
   * @param cameraPresetNumber ID of the preset for the camera
   * @param preset             The preset to be added
   */
  void addPreset(int camera, int cameraPresetNumber, DatabasePreset preset);

  /**
   * Delete a preset from the database.
   *
   * @param camera             ID of the camera
   * @param cameraPresetNumber ID of the preset for the camera
   */
  void deletePreset(int camera, int cameraPresetNumber);

  /**
   * Update a preset to the database.
   *
   * @param camera             ID of the camera
   * @param cameraPresetNumber ID of the preset for the camera
   * @param preset             The preset to be updated
   */
  void updatePreset(int camera, int cameraPresetNumber, DatabasePreset preset);

  /**
   * Returns a preset of the camera.
   *
   * @param camera             ID of the camera
   * @param cameraPresetNumber ID of the preset of the camera
   * @return A preset
   */
  DatabasePreset getPreset(int camera, int cameraPresetNumber);

  /**
   * Returns all the presets.
   *
   * @return all the presets
   */
  ArrayList<DatabasePreset> getAllPresets();

  /**
   * Returns all the presets of the camera.
   *
   * @param cameraId ID of the camera
   * @return the presets of the given camera
   */
  ArrayList<DatabasePreset> getAllPresetsCamera(int cameraId);

  /**
   * Tries to connect to database server.
   *
   * @return True if the connection is succeeded.
   */
  boolean connectToDatabaseServer();

  /**
   * Check if the database is present in the server.
   *
   * @return True if the database is present, false otherwise
   */
  boolean checkDatabase();

  /**
   * Creates new database, overrides the old one if there is one.
   */
  void resetDatabase();

  /**
   * Closes the connection to the server.
   */
  void closeConnection();

  /**
   * Adds a camera to the database.
   *
   * @param id The ID of the camera
   * @param ip The IP of the camera
   */
  void addCamera(int id, int ip, String name);

}
