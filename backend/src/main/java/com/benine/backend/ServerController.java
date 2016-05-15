package com.benine.backend;

import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.database.Database;
import com.benine.backend.database.MySQLDatabase;
import com.benine.backend.http.HttpController;

import java.io.File;



public class ServerController {
  
  private Logger logger;

  private Config config;

  private CameraController cameraController;
  
  private Database database;
  
  private boolean running;

  private HttpController httpController;
  
  /**
   * Constructor of the server controller.
   * Sets up everything needed to run the server.
   */
  public ServerController() {
    setupLogger();
    config = getConfig();
    
    // Setup camerahandler
    cameraController = new CameraController(this);
    
    startupDatabase();
    
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink(config.getValue("camera1"));
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setStreamLink(config.getValue("camera1"));
    cameraController.addCamera(camera);
    cameraController.addCamera(camera2);
    
    

    //    //TODO Cameras has to be in the database when created and create sample presets.
    //    try {
    //      database.addCamera(1, "183.5.1.50:80");
    //      Preset preset = new Preset(new Position(60, 50), 40, 30, 20, false, 30, 2, false);
    //      preset.setImage("/static/presets/preset1_1.jpg");
    //      getCameraController().addPreset(1, preset);
    //      Preset preset2 = new Preset(new Position(60, 50), 40, 30, 20, false, 30, 2, false);
    //      preset2.setImage("/static/presets/preset1_1.jpg");
    //      getCameraController().addPreset(1, preset2);
    //    } catch (SQLException e) {
    //      e.printStackTrace();
    //    }   
  }
  
  
  /**
   * Start the server.
   */
  public void start() {
    httpController = new HttpController(config.getValue("serverip"),
        Integer.parseInt(config.getValue("serverport")), logger, cameraController);
    
    running = true;
    
    try {
      while (running) {
        Thread.sleep(100);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.log("Exception occured: Interrupted while trying to start server",
              LogEvent.Type.CRITICAL);
    }
  }
  
  /**
   * Stop the server.
   */
  public void stop() {
    running = false;
    httpController.destroy();
  }
  
  /**
   * Create database if non exists and make the connection.
   */
  private void startupDatabase() {
    database = new MySQLDatabase(config.getValue("sqluser"), config.getValue("sqlpassword"));
    database.connectToDatabaseServer();
    //If the database does not exist yet, create a new one
    //    if (!database.checkDatabase()) {
    database.resetDatabase();
    // }
  }
  
  /**
   * Setup a new logger.
   */
  private void setupLogger() {
    try {
      logger = new Logger();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Read the main config file.
   * @return config object.
   */
  public static Config getConfig() {
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
  public CameraController getCameraController() {
    return cameraController;
  }
  
  /**
   * Getter for the database.
   * @return the database
   */
  public Database getDatabase() {
    return database;
  }

  /**
   * Setter for the database.
   * @param newDatabase the new database
   */
  public void setDatabase(Database newDatabase) {
    database = newDatabase;
  }

  
  /**
   * Getter for the logger.
   * @return the logger.
   */
  public Logger getLogger() {
    return logger;
  }
}
