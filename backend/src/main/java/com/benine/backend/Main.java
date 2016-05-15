package com.benine.backend;

import java.io.File;

public class Main {
  
  /**
   * Main method of the program.
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    ServerController server = new ServerController("configs" + File.separator + "main.conf");
    server.start();
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
