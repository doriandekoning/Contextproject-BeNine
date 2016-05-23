package com.benine.backend;

import com.benine.backend.http.jetty.HTTPStreamServer;

public class Main {

  /**
   * Main method of the program.
   *
   * @param args command line arguments.
   * @throws Exception If the server cannot start, rendering the application useless.
   */
  public static void main(String[] args) throws Exception {
    ServerController server = ServerController.getInstance();


    server.start();

    new HTTPStreamServer(3725);

    try {
      while (true) {
        Thread.sleep(100);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
