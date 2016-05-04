package com.benine.backend.camera;

import java.io.File;
import java.io.IOException;

import com.benine.backend.LogEvent;
import com.benine.backend.LogWriter;
import com.benine.backend.camera.ipcameracontrol.FocussingIPCamera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.camera.ipcameracontrol.IrisIPCamera;
import com.benine.backend.camera.ipcameracontrol.MovingIPCamera;
import com.benine.backend.camera.ipcameracontrol.ZoomingIPCamera;

/**
 * Class to create camera objects.
 * @author Bryan
 *
 */
public class CameraCreator {
	
	LogWriter logger;
  
  /**
   * Constructor of the camera handler.
   */
  public CameraCreator(){
	  // Setup logger
	  try {
	      logger = new LogWriter("logs" + File.separator + "mainlog");
	  } catch(IOException e) {
		  System.out.println("Cannot create log file");
	      e.printStackTrace();
	  }
  }
  
  /**
   * Creates a camera object as specified in camSpec.
   * @param camSpec specification of the camera 0: type, 1: additional info.
   * @return Camera object.
   * @throws InvalidCameraTypeException when specified camera type can not be created.
   */
  public Camera createCamera(String[] camSpec) throws InvalidCameraTypeException {
    switch (camSpec[0]) {
      case "ipcamera" : logger.write("Create IP camera object", LogEvent.Type.INFO); 
      					return createIpcamera(camSpec[1]);
      default: logger.write("Create IP camera object", LogEvent.Type.WARNING);
      			throw new InvalidCameraTypeException("Type of camera is not right specified");
    }
  }
  
  /**
   * Creates a IP camera object with the IP as specified.
   * @param ipaddress the address of this camera.
   * @return Camera object.
   */
  public Camera createIpcamera(String ipaddress) {
    Camera ipcamera = new IPCamera(ipaddress);
    Camera zoomcam = new ZoomingIPCamera(ipcamera);
    Camera focuscam = new FocussingIPCamera(zoomcam);
    Camera iriscam = new IrisIPCamera(focuscam);
    return new MovingIPCamera(iriscam);
  }
  
  /**
   * Exception thrown when camera object type is not available.
   */
  public static class InvalidCameraTypeException extends Exception {
    
    /**
     * Serial version.
     */
    private static final long serialVersionUID = 10863715123938677L;

    public InvalidCameraTypeException(String reason) {
      super(reason);
    }
  }

}
