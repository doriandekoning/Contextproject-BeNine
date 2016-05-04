package com.benine.backend;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
/**
 * Created by dorian on 28-4-16.
 */
public class CameraHandler implements HttpHandler {

  private LogWriter logger;

  public CameraHandler() {
    // Setup logger
    try {
      logger = new LogWriter("logs" + File.separator + "mainlog");
    }catch(IOException e) {
      System.out.println("Cannot create log file");
      e.printStackTrace();
    }
  }

  public void handle(HttpExchange ext) throws IOException {
    // TODO implement camerastuff
    logger.write("Received request at: " + ext.getRequestURI(), LogEvent.Type.INFO);
    if (ext.getRequestURI().getPath().equals("/getCameraInfo")) {
      String response = "{\"id\":\"12\"}";
      ext.sendResponseHeaders(200, response.length());
      OutputStream output = ext.getResponseBody();
      output.write(response.getBytes());
      output.close();
    } else if (ext.getRequestURI().getPath().equals("/moveCamera")) {
      String[] query = ext.getRequestURI().getQuery().split("&");
      for (int i = 0; i < query.length; i ++ ) {
        String[] split = query[i].split("=");
        if(split[0].equals("pitch")) {
          //TODO impelement set pitch to something for camera here
          System.out.println("pitch is:" + split[1]);
        }
      }
      System.out.println("3");
      String response = "{\"succes\":\"true\"}";
      ext.sendResponseHeaders(200, response.length());
      OutputStream output = ext.getResponseBody();
      System.out.println("5");
      output.write(response.getBytes());
      output.close();
    } else {
      String response = "Hello there!";
      ext.sendResponseHeaders(200, response.length());
      OutputStream output = ext.getResponseBody();
      output.write(response.getBytes());
      output.close();
    }
  }
}