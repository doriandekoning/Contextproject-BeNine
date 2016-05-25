package com.benine.backend.database;

import com.benine.backend.Preset;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Interface for communication with the database.
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
   * @param presetID ID of the camera
   * @throws SQLException No right connection found
   */
  void deletePreset(int presetID) throws SQLException;

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
   * Makes sure the right database is used.
   * @throws SQLException No right connection found
   */
  void useDatabase() throws SQLException;

  /**
   * Adds a tag to the database.
   * @param name The tag
   * @throws SQLException No right connection found
   */
  void addTag(String name) throws SQLException;

  /**
   * Deletes a tag from the database.
   * @param name The tag
   * @throws SQLException No right connection found
   */
  void deleteTag(String name) throws SQLException;

  /**
   * Adds a tag to the database.
   * @return The tags in a list
   * @throws SQLException No right connection found
   */
  Collection<String> getTags() throws SQLException;
}
