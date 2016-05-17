package com.benine.backend;

import com.benine.backend.video.MJPEGStreamReader;

import java.io.IOException;
import java.net.URL;

public class Main {
  
  /**
   * Main method of the program.
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    ServerController server = ServerController.getInstance();

    server.start();

    try {
      new MJPEGStreamReader(new URL("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=2&scale=100&buffer=100"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      while (true) {
        Thread.sleep(100);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }    
}
