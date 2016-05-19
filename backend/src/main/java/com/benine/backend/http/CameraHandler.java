package com.benine.backend.http;

import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jochem on 19-05-16.
 */
public class CameraHandler extends HandlerWrapper {

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

    int camID = getCameraID(s);

    if (checkValidCamerID(camID)) {
      new CameraStreamHandler(camID).handle(s, request, httpServletRequest, httpServletResponse);
    } else {
      httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
      request.setHandled(true);
    }
  }

  private String[] splitPathInfo(String s) {
    return s.split("/");
  }

  private boolean checkValidCamerID(int id) {
    ServerController controller = ServerController.getInstance();
    Camera camera = controller.getCameraController().getCameraById(id);

    return id > 0 && camera != null;
  }

  private int getCameraID(String path) {
    String[] pathSplit = splitPathInfo(path);
    int camID = -1;

    if (pathSplit.length > 1) {
      try {
        return Integer.parseInt(pathSplit[1]);
      } catch (NumberFormatException e) {
        // Input invalid.
      }
    }

    return camID;
  }

}
