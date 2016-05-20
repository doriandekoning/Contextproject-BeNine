package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.database.Database;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 4-5-16.
 */

public abstract class RequestHandler implements HttpHandler {

  /**
   * Decodes the given (decoded) uri into an attributes table
   * @param uri the uri to parse.
   * @return an attributes object containing key->value(string->string)
   *          pairs for the uri parameters.
   * @throws MalformedURIException when the uri is not well parsed.
   */
  public Attributes parseURI(String uri) throws MalformedURIException {
    Attributes params = new Attributes();
    if (uri == null) {
      return params;
    }
    for (String pair : uri.split("&")) {
      String[] splitPair = pair.split("=");
      if (params.containsKey(new Attributes.Name(splitPair[0]))) {
        throw new MalformedURIException("Multiple occurences of parameter with name: "
                                            + splitPair[0]);
      }
      if (splitPair.length < 2) {
        throw new MalformedURIException("Nothing after =");
      }
      params.putValue(splitPair[0], splitPair[1]);
    }
    return params;
  }

  /**
   * Formats the response message as a success.
   * @param exchange the HttpExchange.
   */

  public void respondSuccess(HttpExchange exchange) {
    respond(exchange, "{\"succes\":\"true\"}");
  }
  
  /**
   * Formats the response message as a failure.
   * @param exchange the HttpExchange.
   */
  public void respondFailure(HttpExchange exchange) {
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
    return ServerController.getInstance().getLogger();
  }
}
