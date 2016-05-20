package com.benine.backend.http.jetty;

import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CameraHandler extends CameraRequestHandler {

  private CameraStreamHandler streamHandler;

  /**
   * Constructor for a new CameraHandler, handling the /camera/ request.
   */
  public CameraHandler() {
    this.streamHandler = new CameraStreamHandler();
  }

  @Override
  public void handle(String s, Request request,
                     HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {

    String cameraInfo = getCameraController().getCamerasJSON();

    if (checkValidCameraID(request)) {
      String route = getRoute(request);

      switch (route) {
        case "mjpeg":
          streamHandler.handle(s, request, httpServletRequest, httpServletResponse);
          break;
        default:
          respond(request, httpServletResponse, cameraInfo);
          break;
      }

    } else {
      respond(request, httpServletResponse, cameraInfo);
    }

    request.setHandled(true);

  }

  /**
   * Checks if the camera id of this request is valid.
   * @param request   The current request.
   * @return          True if valid, False if invalid.
   */
  private boolean checkValidCameraID(Request request) {
    int id = getCameraId(request);

    ServerController controller = ServerController.getInstance();
    Camera camera = controller.getCameraController().getCameraById(id);

    return id > 0 && camera != null;
  }

}
