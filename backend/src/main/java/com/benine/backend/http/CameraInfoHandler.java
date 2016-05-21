package com.benine.backend.http;

import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.benine.backend.video.StreamType;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CameraInfoHandler extends CameraRequestHandler {

  private CameraStreamHandler streamHandler;

  private CameraFocusHandler focusHandler;

  private CameraMovingHandler moveHandler;

  private CameraZoomHandler zoomHandler;

  /**
   * Constructor for a new CameraInfoHandler, handling the /camera/ request.
   */
  public CameraInfoHandler() {
    this.streamHandler = new CameraStreamHandler();
    this.focusHandler = new CameraFocusHandler();
    this.moveHandler = new CameraMovingHandler();
    this.zoomHandler = new CameraZoomHandler();
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
          if (cam.getStreamType() == StreamType.MJPEG) {
            streamHandler.handle(s, request, req, res);
          }
          break;
        case "focus":
          if (cam instanceof FocussingCamera) {
            focusHandler.handle(s, request, req, res);
          }
          break;
        case "move":
          if (cam instanceof MovingCamera) {
            moveHandler.handle(s, request, req, res);
          }
          break;
        case "iris":
          if (cam instanceof IrisCamera) {
            focusHandler.handle(s, request, req, res);
          }
          break;
        case "zoom":
          if (cam instanceof ZoomingCamera) {
            zoomHandler.handle(s, request, req, res);
          }
          break;
      }
    }

    // If no route has been selected,
    // return all camera info.
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
