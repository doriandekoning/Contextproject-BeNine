package com.benine.backend;

import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.http.CameraInfoHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

  private static Logger logger;

  private static Config mainConfig;

  private static CameraController cameraController;

  public static void main(String[] args) {
    // TODO cleanup, hacked something together here
    mainConfig = getConfig();

    // TODO Switch adress and max backlog to config
    InetSocketAddress address = new InetSocketAddress("localhost", 8888);

    // Setup camerahandler
    cameraController = new CameraController();
    cameraController.addCamera(new IPCamera(mainConfig.getValue("camera1IP")));
    cameraController.addCamera(new IPCamera(mainConfig.getValue("camera2IP")));
    try {
      logger = new Logger();
    }catch (Exception e) {
      e.printStackTrace();
    }

    try {
      HttpServer server = HttpServer.create(address, 10);
      server.createContext("/getCameraInfo", new CameraInfoHandler());
      logger.log("Server running at: " + server.getAddress(), LogEvent.Type.INFO);
      server.start();
      while(true) {
        Thread.sleep(100);
      }
    } catch (Exception e) {
      logger.log("Unable to start server", LogEvent.Type.CRITICAL);
    }

  }

  public static Config getConfig() {
    // Read config file
    ConfigReader cfReader = new ConfigReader();
    try {
      return cfReader.readConfig("config" +File.separator + "main.conf");
    }catch(Exception e) {
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
