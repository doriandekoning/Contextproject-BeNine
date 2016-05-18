package com.benine.backend;

import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.database.Database;
import com.benine.backend.database.MySQLDatabase;
import com.benine.backend.http.HttpController;
import com.benine.backend.video.StreamController;

import java.io.File;

/**
 * Class containing the elements to make the server work.
 */
public class ServerController {
  
  private static ServerController serverController;
  
  private static String mainConfigPath = "configs" + File.separator + "main.conf";
  
  private Logger logger;

  private Config config;

  private CameraController cameraController;

  private StreamController streamController;

  private Database database;
  
  private boolean running;

  private HttpController httpController;
  
  /**
   * Constructor of the server controller.
   * Sets up everything needed to run the server.
   * @param configPath path to the main config file.
   */
  private ServerController(String configPath) {
    config = setUpConfig(configPath);
    running = false;
    setupLogger();
    
    database = loadDatabase();
    
    cameraController = new CameraController();
    streamController = new StreamController();
  }
  
  /**
   * Get the unique instance of the servercontroller.
   * If it does not yet exists create one.
   * @return unique instance of the servercontroller.
   */
  public static synchronized ServerController getInstance() {
    if (serverController == null) {
      serverController = new ServerController(mainConfigPath);
    }
    return serverController;
  }
  
  
  /**
   * Start the server.
   */
  public void start() {
    httpController = new HttpController(config.getValue("serverip"),
        Integer.parseInt(config.getValue("serverport")), logger); 
    
    startupDatabase();
    loadCameras();

    running = true;
    getLogger().log("Server started", LogEvent.Type.INFO);
  }
  
  /**
   * Stop the server.
   */
  public void stop() {
    if (running) {
      httpController.destroy();
      database.closeConnection();
      running = false;
      getLogger().log("Server stopped", LogEvent.Type.INFO);
    }
  }
  
  /**
   * Load camera's in camera controller.
   * For now it just adds 2 simple camera's.
   */
  private void loadCameras() {
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink(config.getValue("camera1"));
    SimpleCamera camera2 = new SimpleCamera();
    camera2.setStreamLink(config.getValue("camera1"));
    cameraController.addCamera(camera);
    cameraController.addCamera(camera2);  
  }
  
  /**
   * Read the login information from the database and create database object..
   * @return database object
   */
  private Database loadDatabase() {
    String user = config.getValue("sqluser");
    String password = config.getValue("sqlpassword");
    return new MySQLDatabase(user, password, logger);
  }
  
  /**
   * Create database if non exists and make the connection.
   */
  private void startupDatabase() {
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
      logger = new Logger(config.getValue("standardloglocation"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Read the main config file.
   * @param configPath to the main config file.
   * @return config object.
   */
  public Config setUpConfig(String configPath) {
    try {
      return ConfigReader.readConfig(configPath);
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
   * Returns the streamController.
   * @return the streamController
   */
  public StreamController getStreamController() {
    return streamController;
  }


  /**
   * Sets the cameraController.
   * @param cameraController the cameracontroller
   */
  public void setCameraController(CameraController cameraController) {
    this.cameraController = cameraController;
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
  
  /**
   * Getter for the logger.
   * @param logger object to set.
   */
  public void setLogger(Logger logger) {
    this.logger = logger;
  }
  
  /**
   * Test if the server is running.
   * @return true if server is running.
   */
  public Boolean isServerRunning() {
    return running;
  }

  
  /**
   * Get the main config file.
   * @return the config file.
   */
  public Config getConfig() {
    return config;
  }
  
  /**
   * Sets the main config path used on creation of the server controller.
   * @param configPath to the main config file.
   */
  public static void setConfigPath(String configPath) {
    mainConfigPath = configPath;
  }
}
