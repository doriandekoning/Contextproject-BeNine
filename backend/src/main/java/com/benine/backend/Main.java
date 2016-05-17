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
      new MJPEGStreamReader(new URL("http://tuincam.bt.tudelft.nl/mjpg/video.mjpg"));
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
