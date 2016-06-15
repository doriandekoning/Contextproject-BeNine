package com.benine.backend.http.camerahandlers;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.*;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.http.MalformedURIException;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 21-05-16.
 */
public class CameraMovingHandler extends CameraRequestHandler {

  /**
   * Constructs the camera moving handler for this server.
   * @param httpserver to create the handler for.
   */
  public CameraMovingHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    int camID = getCameraId(request);

    MovingCamera movingCam = (MovingCamera) getCameraController().getCameraById(camID);
    String moveType = request.getParameter("moveType");
    String pan = request.getParameter("pan");
    String tilt = request.getParameter("tilt");
    String panSpeed = request.getParameter("panSpeed");
    String tiltSpeed = request.getParameter("tiltSpeed");
    Boolean succes = true;
    
    try {
      move(movingCam, moveType, pan, tilt, panSpeed, tiltSpeed);
    } catch (MalformedURIException e) {
      getLogger().log("Malformed URI: " + request.getRequestURI(), LogEvent.Type.WARNING);
      succes = false;
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera: " + movingCam.getId(), LogEvent.Type.WARNING);
      succes = false;
    } catch (NumberFormatException e) {
      getLogger().log(e.toString(), LogEvent.Type.WARNING);
      succes = false;
    } catch (CameraBusyException e) {
      getLogger().log("Trying to move busy camera with id: " + camID, LogEvent.Type.WARNING);
      succes = false;
    }
    respond(request, res, succes);
    request.setHandled(true);
  }

  /**
   * Moves the camera.
   * @param movingCam     a MovingCamera object.
   * @param moveType      The type of movement.
   * @param pan           The pan value.
   * @param tilt          The tilt value.
   * @param panSpeed      The panspeed value.
   * @param tiltSpeed     The tiltspeed value.
   * @throws MalformedURIException      If the url contains the wrong parameters.
   * @throws CameraConnectionException  If the camera cannot be reached.
   * @throws CameraBusyException        If the camera is busy.
   */
  public void move(MovingCamera movingCam, String moveType, String pan, String tilt,
                   String panSpeed, String tiltSpeed)
          throws MalformedURIException, CameraConnectionException, CameraBusyException {

    if (pan == null || tilt == null || panSpeed == null || tiltSpeed == null) {
      throw new MalformedURIException("Invalid value for moveX or moveY");
    }

    switch (moveType) {
      case "relative": {
        movingCam.move(Integer.parseInt(pan), Integer.parseInt(tilt));
        break;
      }
      case "absolute": {
        Position pos = new Position(Integer.parseInt(pan), Integer.parseInt(tilt));
        movingCam.moveTo(pos, Integer.parseInt(panSpeed), Integer.parseInt(tiltSpeed));
        break;
      }
      default: {
        throw new MalformedURIException("Invalid value for moveType");
      }
    }
  }

  @Override
  boolean isAllowed(Camera cam) {
    return cam instanceof MovingCamera;
  }
}