package com.benine.backend;

import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.database.Database;
import com.benine.backend.database.MySQLDatabase;
import com.benine.backend.http.HttpController;


import java.io.File;
import java.sql.SQLException;

public class Main {

  private static Logger logger;

  private static Config mainConfig;

  private static CameraController cameraController;
  
  private static Database database;

  @SuppressWarnings("unused")
  private static HttpController httpController;

  /**
   * Main method of the program.
   * @param args command line arguments.
   * @throws SQLException When sql statement failes.
   */
  public static void main(String[] args) {
    // TODO cleanup, hacked something together here
    mainConfig = getConfig();



    // Setup camerahandler
    cameraController = new CameraController();
    
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink(mainConfig.getValue("camera1"));
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setStreamLink(mainConfig.getValue("camera1"));
    cameraController.addCamera(camera);
    cameraController.addCamera(camera2);
    try {
      logger = new Logger();
    } catch (Exception e) {
      e.printStackTrace();
    }

    /////CONNECT TO DATABASE SERVER
    database = new MySQLDatabase();
    database.connectToDatabaseServer(); //Connect to the server
    //If the database does not exist yet, create a new one
    //    if (!database.checkDatabase()) {
    database.resetDatabase();
    //}


    //TODO Cameras has to be in the database when created and create sample presets.
    try {
      database.addCamera(1, "183.5.1.50:80");
      Preset preset = new Preset(new Position(60, 50), 40, 30, 20, false, 30, 2, false);
      preset.setImage("/static/presets/preset1_1.jpg");
      getCameraController().addPreset(1, preset);
      Preset preset2 = new Preset(new Position(60, 50), 40, 30, 20, false, 30, 2, false);
      preset2.setImage("/static/presets/preset1_1.jpg");
      getCameraController().addPreset(1, preset2);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    httpController = new HttpController(mainConfig.getValue("serverip"),
        Integer.parseInt(mainConfig.getValue("serverport")), logger, cameraController);

    try {
      while (true) {
        Thread.sleep(100);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.log("Exception occured: Interrupted while trying to start server",
              LogEvent.Type.CRITICAL);
    }
  }

  /**
   * Get the main config file.
   * @return config object.
   */
  public static Config getConfig() {
    // Read config file
    try {
      return ConfigReader.readConfig("configs" + File.separator + "main.conf");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns the cameraController.
   * @return the cameracontroller
   */
  public static CameraController getCameraController() {
    return cameraController;
  }
  
  /**
   * Getter for the database.
   * @return the database
   */
  public static Database getDatabase() {
    return database;
  }

  /**
   * Setter for the database.
   * @param newDatabase the new database
   */
  public static void setDatabase(Database newDatabase) {
    database = newDatabase;
  }

  
  /**
   * Getter for the logger.
   * @return the logger.
   */
  public static Logger getLogger() {
    return logger;
  }

}
