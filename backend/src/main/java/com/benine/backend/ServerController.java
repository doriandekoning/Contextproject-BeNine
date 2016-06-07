package com.benine.backend;

import com.benine.backend.camera.CameraController;
import com.benine.backend.database.DatabaseController;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.performance.PresetQueueController;
import com.benine.backend.preset.PresetController;
import com.benine.backend.video.StreamController;

import java.io.File;

/**
 * Class containing the elements to make the server work.
 */
public class ServerController {

  private static volatile  ServerController serverController;

  private static String mainConfigPath = "configs" + File.separator + "main.conf";

  private final Logger logger;

  private final Config config;

  private static CameraController cameraController;

  private static StreamController streamController;
  
  private static DatabaseController databaseController;
  
  private static PresetQueueController presetQueueController;

  private boolean running;

  private HTTPServer httpServer;

  private static PresetController presetController;

  /**
   * Constructor of the server controller.
   * Sets up everything needed to run the server.
   *
   * @param configPath path to the main config file.
   */
  private ServerController(String configPath) {
    config = setUpConfig(configPath);
    running = false;
    logger = setupLogger(); 
  }
  
  /**
   * Set the controllers.
   */
  private static void loadControllers() {
    databaseController = new DatabaseController();
    
    streamController = new StreamController();

    cameraController = new CameraController();

    presetController = new PresetController();
    
    presetQueueController = new PresetQueueController();
  }

  /**
   * Get the unique instance of the servercontroller.
   * If it does not yet exists create one.
   *
   * @return unique instance of the servercontroller.
   */
  public static ServerController getInstance() {
    if (serverController == null) {
      synchronized (ServerController.class) {
        if (serverController == null) {
          serverController = new ServerController(mainConfigPath);
          loadControllers();
        }
      }
    }
    return serverController;
  }


  /**
   * Start the server.
   *
   * @throws Exception If the server cannot be started.
   */
  public void start() throws Exception {
    cameraController.loadConfigCameras();
    databaseController.start();
    httpServer = new HTTPServer(Integer.parseInt(config.getValue("serverport")), this);

    running = true;

    getLogger().log("Server started", LogEvent.Type.INFO);
  }

  /**
   * Stop the server.
   *
   * @throws Exception If the server cannot be stopped.
   */
  public void stop() throws Exception {
    if (running) {
      httpServer.destroy();
      running = false;
      getLogger().log("Server stopped", LogEvent.Type.INFO);
    }
  }

  /**
   * Setup a new logger.
   * @return logger object at the configs log location.
   */
  private Logger setupLogger() {
    Logger logger = null;
    try {
      logger = new Logger(config.getValue("standardloglocation"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return logger;
  }

  /**
   * Read the main config file.
   *
   * @param configPath to the main config file.
   * @return config object.
   */
  private Config setUpConfig(String configPath) {
    try {
      return ConfigReader.readConfig(configPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns the cameraController.
   *
   * @return the cameracontroller
   */
  public CameraController getCameraController() {
    return cameraController;
  }

  /**
   * Returns the streamController.
   *
   * @return the streamController
   */
  public StreamController getStreamController() {
    return streamController;
  }

  /**
   * Returns the databaseController
   * @return databaseController of this server.
   */
  public DatabaseController getDatabaseController() {
    return databaseController;
  }

  /**
   * Getter for the logger.
   *
   * @return the logger.
   */
  public Logger getLogger() {
    return logger;
  }

  /**
   * Test if the server is running.
   *
   * @return true if server is running.
   */
  public Boolean isServerRunning() {
    return running;
  }

  /**
   * Getter for presetController
   *
   * @return Returns the presetController.
   */
  public PresetController getPresetController() {
    return presetController;
  }
  
  /**
   * Getter for presetQueueController
   *
   * @return Returns the presetQueueController.
   */
  public PresetQueueController getPresetQueueController() {
    return presetQueueController;
  }

  /**
   * Get the main config file.
   *
   * @return the config file.
   */
  public Config getConfig() {
    return config;
  }

  /**
   * Sets the main config path used on creation of the server controller.
   *
   * @param configPath to the main config file.
   */
  public static void setConfigPath(String configPath) {
    mainConfigPath = configPath;
  }
}
