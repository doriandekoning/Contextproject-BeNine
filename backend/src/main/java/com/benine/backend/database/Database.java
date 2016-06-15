package com.benine.backend.database;

import com.benine.backend.camera.Camera;
import com.benine.backend.performance.PresetQueue;
import com.benine.backend.preset.Preset;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

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
   *
   * @param preset The preset to be added
   */
  void addPreset(Preset preset);

  /**
   * Delete a preset from the database.
   *
   * @param presetID of the preset to delete.
   */
  void deletePreset(int presetID);

  /**
   * Update a preset to the database.
   *
   * @param preset The preset to be updated
   */
  void updatePreset(Preset preset);

  /**
   * Returns all the presets.
   *
   * @return all the presets
   */
  ArrayList<Preset> getAllPresets();

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
  void addCamera(int id, String ip);

  /**
   * Checks if cameras are correct in database.
   * @param cameras list of cameras to verify with database.
   */
  void checkCameras(ArrayList<Camera> cameras);

  /**
   * deletes a camera from the database.
   *
   * @param cameraID the camera to be deleted
   */
  void deleteCamera(int cameraID);

  /**
   * Makes sure the right database is used.
   */
  void useDatabase();

  /**
   * Adds a tag to the database.
   *
   * @param name The tag
   * @throws SQLException No right connection found
   */
  void addTag(String name);

  /**
   * Deletes a tag from the database.
   *
   * @param name The tag
   * @throws SQLException No right connection found
   */
  void deleteTag(String name);

  /**
   * Adds a tag to the database.
   *
   * @return The tags in a list
   * @throws SQLException No right connection found
   */
  Collection<String> getTags();

  /**
   * Setter for the connection.
   *
   * @param connection The new connection
   */
  void setConnection(Connection connection);

  /**
   * Get the tags from a preset.
   *
   * @param presetID of the preset to get the tags from.
   * @return The list of tags
   */
  Set<String> getTagsFromPreset(int presetID);

  /**
   * Adds a tag to a preset.
   *
   * @param tag The tag
   * @param presetID of the preset to add the tag to.
   */
  void addTagToPreset(String tag, int presetID);

  /**
   * Deletes a tag to a preset.
   *
   * @param tag The tag
   * @param presetID of the preset to delete the tag from.
   */
  void deleteTagFromPreset(String tag, int presetID);
  
  /**
   * delete the tags from a preset.
   * @param presetID of the preset to delete the tags for.
   */
  void deleteTagsFromPreset(int presetID);

  /**
   * Gets a list of presets belonging to the concert.
   *
   * @param queueID The ID of the concert to get the presets from
   * @return The list of presets
   */
  ArrayList<Preset> getPresetsList(int queueID);

  /**
   * Adds a list of presets to an existing queue.
   *
   * @param presets The list which needs to be added
   * @param queueID The id of the queue
   */
  void addPresetsList(ArrayList<Preset> presets, int queueID);

  /**
   * Deletes a list of presets of an existing queue.
   *
   * @param queueID The id of the queue
   */
  void deletePresetsList(int queueID);

  /**
   * Gets all the queue existing in the database.
   *
   * @return List of the queues
   */
  ArrayList<PresetQueue> getQueues();

  /**
   * Adds a queue to the database.
   *
   * @param queue the queue to be added
   */
  void addQueue(PresetQueue queue);

  /**
   * Deletes a queue from the database.
   *
   * @param ID The ID of the queue
   */
  void deleteQueue(int ID);
}
