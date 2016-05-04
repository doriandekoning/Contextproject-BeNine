package com.benine.backend;

import com.benine.backend.database.Database;
import com.benine.backend.database.MySQLDatabase;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main {

  private static LogWriter logger;

  public static void main(String[] args) {
    // TODO cleanup, hacked something together here

    // TODO Switch adress and max backlog to config
    InetSocketAddress address = new InetSocketAddress("localhost", 8888);

    /////CONNECT TO DATABASE SERVER
    Database database = new MySQLDatabase();
    database.connectToDatabaseServer(); //Connect to the server
    if(!database.checkDatabase()) //If the database does not exist yet, create a new one
      database.resetDatabase();
    /////

    try {
      HttpServer server = HttpServer.create(address, 10);
      server.createContext("/", new  CameraHandler());
      System.out.println("Server running at: " + server.getAddress());
      server.start();
      while(true) {
        Thread.sleep(100);
      }
    } catch (Exception e) {
      logger.write("Unable to start server", LogEvent.Type.CRITICAL);
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
