package com.benine.backend.database;

import com.benine.backend.Preset;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

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
   * @param preset             The preset to be added
   * @throws SQLException No right connection found
   */
  void addPreset(Preset preset) throws SQLException;

  /**
   * Delete a preset from the database.
   * @param camera ID of the camera
   * @throws SQLException No right connection found
   */
  void deletePreset(int camera) throws SQLException;

  /**
   * Update a preset to the database.
   *
   * @param preset             The preset to be updated
   * @throws SQLException No right connection found
   */
  void updatePreset(Preset preset) throws SQLException;

  /**
   * Returns all the presets.
   *
   * @return all the presets
   * @throws SQLException No right connection found
   */
  ArrayList<Preset> getAllPresets() throws SQLException;

  /**
   * Returns all the presets of the camera.
   *
   * @param cameraId ID of the camera
   * @return the presets of the given camera
   * @throws SQLException No right connection found
   */
  ArrayList<Preset> getAllPresetsCamera(int cameraId) throws SQLException;

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
   * @throws SQLException No right connection found
   */
  void addCamera(int id, String ip) throws SQLException;

  /**
   * Gets all the cameras in the database.
   * @return An ArrayList of all te cameras
   * @throws SQLException Wrong connection to database
   */
  ArrayList<Camera> getAllCameras() throws SQLException;

  /**
   * Makes sure the right database is used.
   * @throws SQLException No right connection found
   */
  void useDatabase() throws SQLException;
}
