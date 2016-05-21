package com.benine.backend.http.jetty;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.IrisCamera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 21-05-16.
 */
public class CameraIrisHandler extends CameraRequestHandler {

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    int camID = getCameraId(request);

    IrisCamera irisCam =  (IrisCamera) getCameraController().getCameraById(camID);
    String autoOn = req.getParameter("autoIrisOn");
    String setPos = req.getParameter("position");
    String speed = req.getParameter("speed");

    try {
      if (autoOn != null) {
        boolean autoOnBool = Boolean.parseBoolean(autoOn);
        irisCam.setAutoIrisOn(autoOnBool);
      }
      if (setPos != null) {
        irisCam.setIrisPosition(Integer.parseInt(setPos));
      } else if (speed != null) {
        irisCam.moveIris(Integer.parseInt(speed));
      }
      respondSuccess(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera: " + irisCam.getId(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }
  }
}
