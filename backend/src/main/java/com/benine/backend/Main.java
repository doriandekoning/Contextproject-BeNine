package com.benine.backend;

import com.sun.net.httpserver.HttpServer;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

  private static LogWriter logger;

  public static void main(String[] args) {
    // TODO cleanup, hacked something together here
    // Setup logger
    System.out.println("Im freerereeeee");

    try {
      logger = new LogWriter("logs" + File.separator + "mainlog");
    }catch(IOException e) {
      System.out.println("Cannot create log file");
      e.printStackTrace();
    }
    // TODO Switch adress and max backlog to config
    InetSocketAddress adress = new InetSocketAddress(8888);
    HttpServer server = new HttpServer.create(adress, 10);

    try {
      logger.write(new LogEvent(System.currentTimeMillis() + "",
              "Starting http server at ip adress: " + config.getValue("ipadress")
                      + ":" + config.getValue("port"), LogEvent.Type.INFO));
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Config getConfig() {
    // Read config file
    ConfigReader cfReader = new ConfigReader();
    try {
      return cfReader.readConfig("config" +File.separator + "main.conf");
    }catch(Exception e) {
      e.printStackTrace();
      try {
        logger.write(new LogEvent(System.currentTimeMillis() + "",
                "Cannot read main config file",
                LogEvent.Type.CRITICAL));
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    return null;
  }
}
