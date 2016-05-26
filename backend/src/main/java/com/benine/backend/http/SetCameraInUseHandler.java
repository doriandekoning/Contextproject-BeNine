package com.benine.backend.http;

import com.benine.backend.camera.Camera;
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

    String autoOn = request.getParameter("inuse");

    if (isAllowed(cam) && autoOn != null && !cam.isInUse()) {
      if(Boolean.parseBoolean(autoOn)) {
        cam.setInUse();
      } else {
        cam.setNotInUse();
      }
      respondSuccess(request, res);
    } else {
      respondFailure(request, res);
    }

    request.setHandled(true);
  }


  @Override
  boolean isAllowed(Camera cam) {
    return cam != null;
  }
}
