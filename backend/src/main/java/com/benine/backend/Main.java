package com.benine.backend;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

public class Main {

  /**
   * Main method of the program.
   *
   * @param args command line arguments.
   * @throws CameraConnectionException 
   */
  public static void main(String[] args) throws CameraConnectionException {
//    ServerController server = ServerController.getInstance();
//
//
//    server.start();
//
//    try {
//      while (true) {
//        Thread.sleep(100);
//      }
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
    IPCamera camera = new IPCamera("192.168.10.101");
    camera.moveIris(99);
    System.out.println(camera.getIrisPosition());
  }
}
