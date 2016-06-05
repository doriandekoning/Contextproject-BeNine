package com.benine.backend.database;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;

import java.sql.SQLException;

/**
 * Controls the database, to start and stop it.
 *
 */
public class DatabaseController {
  
  private MySQLDatabase database;
  
  private ServerController serverController;
  
  private Config config;
  
  private Logger logger;
  
  /**
   * Constructor for a database controller.
   * @param serverController to interact with the rest of the system
   */
  public DatabaseController(ServerController serverController) {
    this.serverController = serverController;
    this.config = serverController.getConfig();
    this.logger = serverController.getLogger();
    database = loadDatabase();
  }
  
  /**
   * Read the login information from the database and create database object..
   *
   * @return database object
   */
  private MySQLDatabase loadDatabase() {
    String user = config.getValue("sqluser");
    String password = config.getValue("sqlpassword");
    return new MySQLDatabase(user, password, logger);
  }
  

  /**
   * Create database if non exists and make the connection.
   */
  public void start() {
    database.connectToDatabaseServer();
    if (!database.checkDatabase()) {
      database.resetDatabase();
    } else {
      database.useDatabase();
    }
    database.checkCameras(serverController.getCameraController().getCameras());
    loadPresets();
  }
  
  /**
   * Stops the database connection.
   */
  public void stop() {
    database.closeConnection();
  } 
  
  /**
   * Loads the presets from the database.
   */
  private void loadPresets() {
    PresetController presetController = serverController.getPresetController();
    try {
      presetController.addPresets(database.getAllPresets());
      for (Preset preset : presetController.getPresets()) {
        preset.addTags(database.getTagsFromPreset(preset));
      }
    } catch (SQLException e) {
      logger.log("Cannot read presets from database", LogEvent.Type.CRITICAL);
    }
  }

  /**
   * Getter for the database.
   *
   * @return the database
   */
  public MySQLDatabase getDatabase() {
    return database;
  }
  
  /**
   * Setter for the database also updates the presets and cameras according to new database.
   *
   * @param newDatabase the new database
   */
  public void setDatabase(MySQLDatabase newDatabase) {
    database = newDatabase;
    //loadDatabase();
    //cameraController.loadConfigCameras();
    loadPresets();
  }

}
