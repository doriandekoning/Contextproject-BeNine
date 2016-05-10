package com.benine.backend;

import com.benine.backend.database.Database;
import com.benine.backend.database.MySQLDatabase;

import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.http.CameraInfoHandler;
import com.benine.backend.http.FocussingHandler;
import com.benine.backend.http.IrisHandler;
import com.benine.backend.http.MovingHandler;
import com.benine.backend.http.PresetHandler;
import com.benine.backend.http.ZoomingHandler;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main {

  private static Logger logger;

  private static Config mainConfig;

  private static CameraController cameraController;

  /**
   * Main method of the program.
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    // TODO cleanup, hacked something together here
    mainConfig = getConfig();

    InetSocketAddress address = new InetSocketAddress(mainConfig.getValue("serverip"), 
                                          Integer.parseInt(mainConfig.getValue("serverport")));

    // Setup camerahandler
    cameraController = new CameraController();
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink("tuincam.bt.tudelft.nl/mjpg/video.mjpg");
    cameraController.addCamera(camera);
    //cameraController.addCamera(new IPCamera(mainConfig.getValue("camera2IP")));

    try {
      logger = new Logger();
    } catch (Exception e) {
      e.printStackTrace();
    }

    /////CONNECT TO DATABASE SERVER
    Database database = new MySQLDatabase();
    database.connectToDatabaseServer(); //Connect to the server
    if(!database.checkDatabase()) //If the database does not exist yet, create a new one
      database.resetDatabase();
    /////

    try {
      HttpServer server = HttpServer.create(address, 10);
      // TODO create endpoint like /camera/ to return all camera info
      // TODO create endpoints like /camera/1/ to return camera 1 info
      // TODO create handlers a handler for every camera
      // TODO move handlers to httpHandlerController
      server.createContext("/camera/0/getcamerainfo", new CameraInfoHandler(cameraController, 0));
      server.createContext("/camera/0/focus", new FocussingHandler(cameraController, 0));
      server.createContext("/camera/0/iris", new IrisHandler(cameraController, 0));
      server.createContext("/camera/0/move", new MovingHandler(cameraController, 0));
      server.createContext("/camera/0/zoom", new ZoomingHandler(cameraController, 0));
      server.createContext("/camera/0/preset", new PresetHandler(cameraController, 0));

      logger.log("Server running at: " + server.getAddress(), LogEvent.Type.INFO);
      server.start();
      while (true) {
        Thread.sleep(100);
      }
    } catch (IOException e) {
      logger.log("Unable to start server", LogEvent.Type.CRITICAL);
    } catch (InterruptedException e) {
      logger.log("Unable to start server", LogEvent.Type.CRITICAL);
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
}
