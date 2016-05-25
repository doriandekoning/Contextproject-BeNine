package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomingCamera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 21-05-16.
 */
public class CameraZoomHandler extends CameraRequestHandler {

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    int camID = getCameraId(request);

    ZoomingCamera zoomingCam = (ZoomingCamera) getCameraController().getCameraById(camID);
    String zoomType = request.getParameter("zoomType");
    String zoom = request.getParameter("zoom");

    try {
      zoom(zoomingCam, zoomType, zoom);
    } catch (MalformedURIException e) {
      getLogger().log("Malformed URI: " + request.getRequestURI(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera: " + zoomingCam.getId(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }

    request.setHandled(true);
  }

  /**
   * Zooms the camera.
   * @param zoomingCam  A ZoomingCamera.
   * @param zoomType    The type of zooming operation.
   * @param zoom        The zoom value.
   * @throws MalformedURIException      If there is an error in the request.
   * @throws CameraConnectionException  If the camera cannot be reached.
   */
  private void zoom(ZoomingCamera zoomingCam, String zoomType, String zoom)
          throws MalformedURIException, CameraConnectionException {

    if (zoom != null && zoomType.equals("relative")) {
      zoomingCam.zoom(Integer.parseInt(zoom));
    } else if (zoom != null && zoomType.equals("absolute")) {
      zoomingCam.zoomTo(Integer.parseInt(zoom));
    } else {
      throw new MalformedURIException("Invalid Zoom parameters");
    }
  }

  @Override
  boolean isAllowed(Camera cam) {
    return cam instanceof ZoomingCamera;
  }
}
