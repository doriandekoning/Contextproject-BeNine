package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dorian on 4-5-16.
 */

public abstract class RequestHandler implements HttpHandler {

  private CameraController controller;
  private Logger logger;

  /**
   * Creates a new FocussingHandler.
   * @param controller the cameracontroller to interact with
   * @param logger the logger to be used to log to
   */
  public RequestHandler(CameraController controller, Logger logger) {
    this.controller = controller;
    this.logger = logger;
  }

  /**
   * Decodes the given (decoded) uri into an attributes table
   * @param uri the uri to parse.
   * @return an attributes object containing key->value(string->string)
   *          pairs for the uri parameters.
   * @throws MalformedURIException when the uri is not well parsed.
   */
  public Attributes parseURI(String uri) throws MalformedURIException {
    Attributes params = new Attributes();
    for (String pair : uri.split("&")) {
      String[] splitPair = pair.split("=");
      if (params.containsKey(new Attributes.Name(splitPair[0]))) {
        throw new MalformedURIException("Multiple occurences of parameter with name: "
                                            + splitPair[0]);
      }
      params.putValue(splitPair[0], splitPair[1]);
    }
    return params;
  }

  /**
   * Formats the response message as a success.
   * @param exchange the HttpExchange.
   */
  public void responseSuccess(HttpExchange exchange) {
    respond(exchange, "{\"succes\":\"true\"}");
  }
  
  /**
   * Formats the response message as a failure.
   * @param exchange the HttpExchange.
   */
  public void responseFailure(HttpExchange exchange) {
    respond(exchange, "{\"succes\":\"false\"}");
  } 
  
  
  /**
   * Responds to a request with status 200.
   * @param exchange the exchange to respond to.
   * @param response a string with the response.
   */
  public void respond(HttpExchange exchange, String response) {
    try {
      exchange.sendResponseHeaders(200, response.length());
      OutputStream out = exchange.getResponseBody();
      out.write(response.getBytes("UTF-8"));
      out.close();
    } catch (IOException e) {
      getLogger().log("Error occured while writing the response to a request at URI"
                       + exchange.getRequestURI(), LogEvent.Type.WARNING);
    }


  }

  /**
   * Returns cameracontroller
   * @return cameracontroller interacting with.
   */
  public CameraController getCameraController() {
    return controller;
  }


  /**
   * Fetches camera id from http exchange.
   * @param exchange the http exchange to fix the id from.
   * @return the id of the camera.
   */
  public int getCameraId(HttpExchange exchange) {
    // Get path
    Pattern pattern = Pattern.compile(".*/camera/(\\d*)/.*");
    String path = exchange.getRequestURI().getPath();
    Matcher m = pattern.matcher(path);
    m.matches();
    return Integer.parseInt(m.group(1));
  }

  /**
   * Return the logger.
   * @return Logger.
   */
  public Logger getLogger() {
    return logger;
  }
}
