package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.FocussingCamera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 21-05-16.
 */
public class CameraFocusHandler extends CameraRequestHandler {

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    int camID = getCameraId(request);
    FocussingCamera focusCam = (FocussingCamera) getCameraController().getCameraById(camID);

    String autoOn = request.getParameter("autoFocusOn");
    String setPos = request.getParameter("position");
    String speed = request.getParameter("speed");

    try {
      setFocus(focusCam, autoOn, setPos, speed);
      respondSuccess(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera: " + focusCam.getId(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (NumberFormatException e) {
      getLogger().log(e.toString(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }

    request.setHandled(true);
  }

  /**
   * Sets the focus of the supplied camera.
   * @param focusCam  A Focussingcamera
   * @param autoOn    The autoOn parameter
   * @param setPos    The setPos parameter
   * @param speed     The speed of the focus movement.
   * @throws CameraConnectionException If the camera cannot be reached.
   */
  private void setFocus(FocussingCamera focusCam,
                        String autoOn, String setPos, String speed)
          throws CameraConnectionException {
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
