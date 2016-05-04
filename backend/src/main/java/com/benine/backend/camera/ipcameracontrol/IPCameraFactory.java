package com.benine.backend.camera.ipcameracontrol;

import java.io.File;
import java.io.IOException;

import com.benine.backend.LogEvent;
import com.benine.backend.LogWriter;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraFactory;

/**
 * Class to create camera objects.
 * @author Bryan
 *
 */
public class IPCameraFactory extends CameraFactory {
	
	public static Logger logger = setupLogger();
  
  /**
   * Constructor of the camera handler.
   */
  public IPCameraFactory(){

  }

  /**
   *
   */
  private static Logger setupLogger() {
    // Setup logger
    try {
      return new Logger(new LogWriter("logs" + File.separator + "mainlog"));
    } catch (IOException e) {
      System.out.println("Cannot create log file");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Creates a camera object as specified in camSpec.
   * @param camSpec specification of the camera 0: type, 1: additional info.
   * @return Camera object.
   * @throws InvalidCameraTypeException when specified camera type can not be created.
   */
  public IPCamera createCamera(String[] camSpec) throws InvalidCameraTypeException {
    switch (camSpec[0]) {
      case "ipcamera" : logger.log("Create IP camera object", LogEvent.Type.INFO);
      					return createIpcamera(camSpec[1]);
      default: logger.log("Create IP camera object", LogEvent.Type.WARNING);
      			throw new InvalidCameraTypeException("Type of camera is not right specified");
    }
  }
  
  /**
   * Creates a IP camera object with the IP as specified.
   * @param ipaddress the address of this camera.
   * @return Camera object.
   */
  public IPCamera createIpcamera(String ipaddress) {
    return new IPCamera(ipaddress);
  }
  

}
