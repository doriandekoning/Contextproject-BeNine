package com.benine.backend.http.jetty;

import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.FocussingCamera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CameraInfoHandler extends CameraRequestHandler {

  private CameraStreamHandler streamHandler;

  private CameraFocusHandler focusHandler;

  /**
   * Constructor for a new CameraInfoHandler, handling the /camera/ request.
   */
  public CameraInfoHandler() {
    this.streamHandler = new CameraStreamHandler();
    this.focusHandler = new CameraFocusHandler();
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    String cameraInfo = getCameraController().getCamerasJSON();

    if (checkValidCameraID(request)) {
      String route = getRoute(request);
      Camera cam = getCameraController().getCameraById(getCameraId(request));

      switch (route) {
        case "mjpeg":
          streamHandler.handle(s, request, req, res);
          break;
        case "focus":
          if (cam instanceof FocussingCamera) {
            focusHandler.handle(s, request, req, res);
          }
          break;
      }
    }

    respond(request, res, cameraInfo);
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
