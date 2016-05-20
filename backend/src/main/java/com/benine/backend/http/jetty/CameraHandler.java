package com.benine.backend.http.jetty;

import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jochem on 19-05-16.
 */
public class CameraHandler extends CameraRequestHandler {

  private CameraStreamHandler streamHandler;

  public CameraHandler() {
    this.streamHandler = new CameraStreamHandler();
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
    if (checkValidCameraID(request)) {
      String route = getRoute(request);

      switch (route) {
        case "mjpeg": streamHandler.handle(s, request, httpServletRequest, httpServletResponse);
        case "default": request.setHandled(true);
      }

    } else {
      httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
      request.setHandled(true);
    }
  }

  private boolean checkValidCameraID(Request request) {
    int id = getCameraId(request);

    ServerController controller = ServerController.getInstance();
    Camera camera = controller.getCameraController().getCameraById(id);

    return id > 0 && camera != null;
  }

}
