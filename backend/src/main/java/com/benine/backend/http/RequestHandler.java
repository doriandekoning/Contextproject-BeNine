package com.benine.backend.http;

import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.CameraController;
import com.benine.backend.preset.PresetController;
import com.benine.backend.video.StreamController;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 20-05-16.
 */
public abstract class RequestHandler extends AbstractHandler {
  
  private CameraController cameraController;
  
  private PresetController presetController;
  
  private StreamController streamController;
  
  private Logger logger;
  
  private Config config;
  
  /**
   * Constructs a request handler.
   * @param httpserver to interact with the rest of the system.
   */
  public RequestHandler(HTTPServer httpserver) {
    this.cameraController = httpserver.getCameraController();
    this.presetController = httpserver.getPresetController();
    this.streamController = httpserver.getStreamController();
    this.logger = httpserver.getLogger();
    this.config = httpserver.getConfig();
  }

  /**
   * Responds to a request with status 200.
   * @param request the httpservletrequest to process.
   * @param response the httpservletresponse to respond to.
   * @param body a string with the response.
   */
  public void respond(Request request, HttpServletResponse response, String body) {
    try {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentLength(body.length());

      PrintWriter out = response.getWriter();
      out.write(body);
      out.close();

    } catch (IOException e) {
      getLogger().log("Error occured while writing the response to a request at URI"
              + request.getRequestURI(), LogEvent.Type.WARNING);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Responds a success true JSON.
   * @param request   The request to respond to.
   * @param response  The response
   */
  public void respondSuccess(Request request, HttpServletResponse response) {
    respond(request, response, "{\"succes\":\"true\"}");
  }

  /**
   * Responds a success false JSON.
   * @param request   The request to respond to.
   * @param response  The response
   */
  public void respondFailure(Request request, HttpServletResponse response) {
    respond(request, response, "{\"succes\":\"false\"}");
  }

  /**
   * Returns the ServerController logger.
   * @return  A Logger object.
   */
  protected Logger getLogger() {
    return logger;
  }

  /**
   * Returns the cameraController
   * @return A camera controller object.
   */
  protected CameraController getCameraController() {
    return cameraController;
  }
  
  /**
   * Returns the streamController to get the stream
   * @return stream Controller.
   */
  protected StreamController getStreamController() {
    return streamController;
  }

  /**
   * Returns the presetController.
   * @return presetController to change the presets
   */
  protected PresetController getPresetController() {
    return presetController;
  }
  
  /**
   * Returns the config.
   * @return config object.
   */
  protected Config getConfig() {
    return config;
  }
}
