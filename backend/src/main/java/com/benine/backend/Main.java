package com.benine.backend;

import com.benine.backend.database.Database;
import com.benine.backend.database.MySQLDatabase;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.SimpleCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera2;
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
    
    //Temporary adding multiple test camera's
    SimpleCamera camera = new SimpleCamera();
    camera.setStreamLink("http://tuincam.bt.tudelft.nl/mjpg/video.mjpg");
    cameraController.addCamera(camera);
    Camera camera2 = new IPCamera2("83.128.144.84:88");
    cameraController.addCamera(camera2);
    SimpleCamera camera3 = new SimpleCamera();
    camera3.setStreamLink("http://131.180.123.51/cgi-bin/nph-zms?mode=jpeg&monitor=4&scale=100&maxfps=6&buffer=100");
    cameraController.addCamera(camera3);
    SimpleCamera camera4 = new SimpleCamera();
    camera4.setStreamLink("http://131.180.123.51/cgi-bin/nph-zms?mode=jpeg&monitor=5&scale=100&maxfps=6&buffer=100");
    cameraController.addCamera(camera4);
    SimpleCamera camera5 = new SimpleCamera();
    camera5.setStreamLink("http://tuincam.bt.tudelft.nl/mjpg/video.mjpg");
    cameraController.addCamera(camera5);
    SimpleCamera camera6 = new SimpleCamera();
    camera6.setStreamLink("http://131.180.123.51/cgi-bin/nph-zms?mode=jpeg&monitor=4&scale=100&maxfps=6&buffer=100");
    cameraController.addCamera(camera6);
    SimpleCamera camera7 = new SimpleCamera();
    camera7.setStreamLink("http://131.180.123.51/cgi-bin/nph-zms?mode=jpeg&monitor=5&scale=100&maxfps=6&buffer=100");
    cameraController.addCamera(camera7);
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
      server.createContext("/getCameraInfo", new CameraInfoHandler(cameraController));
      server.createContext("/focus", new FocussingHandler(cameraController));
      server.createContext("/iris", new IrisHandler(cameraController));
      server.createContext("/move", new MovingHandler(cameraController));
      server.createContext("/zoom", new ZoomingHandler(cameraController));
      server.createContext("/preset", new PresetHandler(cameraController));

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
