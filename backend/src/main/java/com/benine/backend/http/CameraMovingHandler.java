package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 21-05-16.
 */
public class CameraMovingHandler extends CameraRequestHandler {

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

    try {
      move(movingCam, moveType, pan, tilt, panSpeed, tiltSpeed);
      respondSuccess(request, res);
    } catch (MalformedURIException e) {
      getLogger().log("Malformed URI: " + request.getRequestURI(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera: " + movingCam.getId(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (NumberFormatException e) {
      getLogger().log(e.toString(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }

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
   */
  public void move(MovingCamera movingCam, String moveType, String pan, String tilt,
                   String panSpeed, String tiltSpeed)
          throws MalformedURIException, CameraConnectionException {

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
}