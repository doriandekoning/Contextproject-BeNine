package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.FocussingCamera;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created on 21-05-16.
 */
public class SetCameraInUseHandler extends CameraRequestHandler {

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    int camID = getCameraId(request);
    Camera cam = getCameraController().getCameraById(camID);

    boolean autoOn = Boolean.parseBoolean(request.getParameter("inuse"));

    try {
      cam.setInUse(autoOn);
      respondSuccess(request, res);
    }  catch (NumberFormatException e) {
      getLogger().log(e.toString(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }

    request.setHandled(true);
  }


  @Override
  boolean isAllowed(Camera cam) {
    return cam instanceof Camera;
  }
}
