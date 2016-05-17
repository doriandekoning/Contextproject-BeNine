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
      new Thread(new MJPEGStreamReader(new URL("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=2&scale=100&buffer=100"))).start();
      new Thread(new MJPEGStreamReader(new URL("http://tuincam.bt.tudelft.nl/mjpg/video.mjpg"))).start();

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
