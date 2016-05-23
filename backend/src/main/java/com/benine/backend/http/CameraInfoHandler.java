package com.benine.backend.http;

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

  private CameraIrisHandler irisHandler;

  private CameraZoomHandler zoomHandler;

  /**
   * Constructor for a new CameraInfoHandler, handling the /camera/ request.
   */
  public CameraInfoHandler() {
    this.streamHandler = new CameraStreamHandler();
    this.focusHandler = new CameraFocusHandler();
    this.irisHandler = new CameraIrisHandler();
    this.moveHandler = new CameraMovingHandler();
    this.zoomHandler = new CameraZoomHandler();
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    String cameraInfo = getCameraController().getCamerasJSON();

    boolean routed = false;

    if (checkValidCameraID(request)) {
      String route = getRoute(request);
      Camera cam = getCameraController().getCameraById(getCameraId(request));

      switch (route) {
        case "mjpeg":
          if (cam.getStreamType() == StreamType.MJPEG) {
            streamHandler.handle(s, request, req, res);
            routed = true;
          }
          break;
        case "focus":
          if (cam instanceof FocussingCamera) {
            focusHandler.handle(s, request, req, res);
            routed = true;
          }
          break;
        case "move":
          if (cam instanceof MovingCamera) {
            moveHandler.handle(s, request, req, res);
            routed = true;
          }
          break;
        case "iris":
          if (cam instanceof IrisCamera) {
            irisHandler.handle(s, request, req, res);
            routed = true;
          }
          break;
        case "zoom":
          if (cam instanceof ZoomingCamera) {
            zoomHandler.handle(s, request, req, res);
            routed = true;
          }
          break;
        default:
          break;
      }
    }

    if (!routed) {
      respond(request, res, cameraInfo);
      request.setHandled(true);
    }
  }

  /**
   * Checks if the camera id of this request is valid.
   * @param request   The current request.
   * @return          True if valid, False if invalid.
   */
  private boolean checkValidCameraID(Request request) {
    int id = getCameraId(request);
    Camera camera = getCameraController().getCameraById(id);

    return id > 0 && camera != null;
  }

  /**
   * Sets the handlers of this cameraInfoHandler
   * @param streamHandler A StreamHandler object.
   * @param focusHandler A FocusHandler object.
   * @param irishandler An IrisHandler object.
   * @param movingHandler A MovingHandler object.
   * @param zoomHandler A ZoomHandler object.
   */
  public void setHandlers(CameraStreamHandler streamHandler,  CameraFocusHandler focusHandler,
                     CameraIrisHandler irishandler,
                     CameraMovingHandler movingHandler,
                     CameraZoomHandler zoomHandler) {

    this.streamHandler = streamHandler;
    this.focusHandler = focusHandler;
    this.moveHandler = movingHandler;
    this.zoomHandler = zoomHandler;
    this.irisHandler = irishandler;
  }
}
