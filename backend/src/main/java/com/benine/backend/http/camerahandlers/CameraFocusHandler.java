package com.benine.backend.http.camerahandlers;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.http.HTTPServer;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 21-05-16.
 */
public class CameraFocusHandler extends CameraRequestHandler {

  /**
   * Constructs the cameraFocus handler.
   * @param httpserver where this handler is for.
   */
  public CameraFocusHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    int camID = getCameraId(request);
    FocussingCamera focusCam = (FocussingCamera) getCameraController().getCameraById(camID);

    String autoOn = request.getParameter("autoFocusOn");
    String setPos = request.getParameter("position");
    String speed = request.getParameter("speed");
    Boolean success = true;
    try {
      setFocus(focusCam, autoOn, setPos, speed);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera: " + focusCam.getId(), LogEvent.Type.WARNING);
      success = false;
    } catch (NumberFormatException e) {
      getLogger().log(e.toString(), LogEvent.Type.WARNING);
      success = false;
    } catch (CameraBusyException e) {
      getLogger().log("Trying to move busy camera with id: " + camID, LogEvent.Type.WARNING);
      success = false;
    }
    respond(request, res, success);
    request.setHandled(true);
  }

  /**
   * Sets the focus of the supplied camera.
   * @param focusCam  A Focussingcamera
   * @param autoOn    The autoOn parameter
   * @param setPos    The setPos parameter
   * @param speed     The speed of the focus movement.
   * @throws CameraConnectionException If the camera cannot be reached.
   * @throws CameraBusyException        If the camera is busy.
   */
  private void setFocus(FocussingCamera focusCam,
                        String autoOn, String setPos, String speed)
          throws CameraConnectionException, CameraBusyException {
    if (autoOn != null) {
      boolean autoOnBool = Boolean.parseBoolean(autoOn);
      focusCam.setAutoFocusOn(autoOnBool);
    }
    if (setPos != null) {
      focusCam.setFocusPosition(Integer.parseInt(setPos));
    } else if (speed != null) {
      focusCam.moveFocus(Integer.parseInt(speed));
    }
  }

  @Override
  boolean isAllowed(Camera cam) {
    return cam instanceof FocussingCamera;
  }
}
