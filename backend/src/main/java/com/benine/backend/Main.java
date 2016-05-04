package com.benine.backend;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

  private static Logger logger;

  public static void main(String[] args) {
    // TODO cleanup, hacked something together here

    // TODO Switch adress and max backlog to config
    InetSocketAddress address = new InetSocketAddress("localhost", 8888);
    getConfig();
    try {
      logger = new Logger();
    }catch (Exception e) {
      e.printStackTrace();
    }

    try {
      HttpServer server = HttpServer.create(address, 10);
      server.createContext("/", new  CameraHandler());
      System.out.println("Server running at: " + server.getAddress());
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
}
