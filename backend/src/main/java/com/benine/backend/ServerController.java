package com.benine.backend;

import com.benine.backend.camera.CameraController;
import com.benine.backend.database.DatabaseController;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.preset.PresetController;
import com.benine.backend.video.StreamController;

import java.io.File;

/**
 * Class containing the elements to make the server work.
 */
public class ServerController {

  private static volatile  ServerController serverController;

  private static String mainConfigPath = "configs" + File.separator + "main.conf";

  private Logger logger;

  private Config config;

  private CameraController cameraController;

  private StreamController streamController;
  
  private DatabaseController databaseController;

  private boolean running;

  private HTTPServer httpServer;

  private PresetController presetController;

  /**
   * Constructor of the server controller.
   * Sets up everything needed to run the server.
   *
   * @param configPath path to the main config file.
   */
  private ServerController(String configPath) {
    config = setUpConfig(configPath);
    running = false;
    setupLogger();

    databaseController = new DatabaseController(this);

    cameraController = new CameraController(this);

    presetController = new PresetController(this);

    streamController = new StreamController();
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
   * Sets the cameraController.
   *
   * @param cameraController the cameracontroller
   */
  public void setCameraController(CameraController cameraController) {
    this.cameraController = cameraController;
  }

  public void setStreamController(StreamController streamController) {
    this.streamController = streamController;
  }
  
  /**
   * Sets the databaseController.
   * @param databaseController the databaseController
   */
  public void setDatabaseController(DatabaseController databaseController) {
    this.databaseController = databaseController;
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
   * Getter for the logger.
   *
   * @param logger object to set.
   */
  public void setLogger(Logger logger) {
    this.logger = logger;
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

  public void setPresetController(PresetController newController) {
    this.presetController = newController;
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
