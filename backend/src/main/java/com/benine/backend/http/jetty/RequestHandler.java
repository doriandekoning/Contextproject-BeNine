package com.benine.backend.http.jetty;

import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.database.Database;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 20-05-16.
 */
public abstract class RequestHandler extends AbstractHandler {

  /**
   * Returns cameracontroller
   * @return cameracontroller interacting with.
   */
  protected CameraController getCameraController() {
    return ServerController.getInstance().getCameraController();
  }

  /**
   * Returns the database
   * @return database to retrieve information from.
   */
  protected Database getDatabase() {
    return ServerController.getInstance().getDatabase();
  }

  /**
   * Responds to a request with status 200.
   * @param response the httpservletresponse to respond to.
   * @param body a string with the response.
   */
  public void respond(HttpServletResponse response, String body) {
    try {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentLength(body.length());

      PrintWriter out = response.getWriter();
      out.write(body);
      out.close();

    } catch (IOException e) {
      //Logger().log("Error occured while writing the response to a request at URI"
      //        + exchange.getRequestURI(), LogEvent.Type.WARNING);
    }
  }
}
