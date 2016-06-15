package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.*;
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

    try {
      move(movingCam, request);
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
    } catch (CameraBusyException e) {
      getLogger().log("Trying to move busy camera with id: " + camID, LogEvent.Type.WARNING);
    }

    request.setHandled(true);
  }

  /**
   * Moves the camera.
   * @param movingCam     a MovingCamera object.
   * @param request to move the camera.
   * @throws MalformedURIException      If the url contains the wrong parameters.
   * @throws CameraConnectionException  If the camera cannot be reached.
   * @throws CameraBusyException        If the camera is busy.
   */
  public void move(MovingCamera movingCam, Request request)
          throws MalformedURIException, CameraConnectionException, CameraBusyException {
    
    String moveType = request.getParameter("moveType");
    String pan = request.getParameter("pan");
    String tilt = request.getParameter("tilt");
    String panSpeed = request.getParameter("panSpeed");
    String tiltSpeed = request.getParameter("tiltSpeed");
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