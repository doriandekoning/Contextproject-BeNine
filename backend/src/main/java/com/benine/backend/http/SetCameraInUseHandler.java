package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created on 21-05-16.
 */
public class SetCameraInUseHandler extends CameraRequestHandler {

  /**
   * Constructs the set camera in use handler for this server.
   * @param httpserver this handler is created for.
   */
  public SetCameraInUseHandler(HTTPServer httpserver) {
    super(httpserver);
  }


  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    int camID = getCameraId(request);
    Camera cam = getCameraController().getCameraById(camID);

    String using = request.getParameter("inuse");

    if (using != null && isAllowed(cam) && cam.isInUse() != Boolean.parseBoolean(using)) {
      if (Boolean.parseBoolean(using)) {
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
