package com.benine.backend;

import com.benine.backend.video.StreamDistributer;
import com.benine.backend.video.StreamNotAvailableException;

public class Main {

  /**
   * Main method of the program.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    ServerController server = ServerController.getInstance();

    server.start();

    try {
      StreamDistributer reader = new StreamDistributer(server.getStreamController().getStreamReader(1));
    } catch (StreamNotAvailableException e) {
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
