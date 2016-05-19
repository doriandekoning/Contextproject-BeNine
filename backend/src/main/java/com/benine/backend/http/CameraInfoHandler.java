package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandler extends RequestHandler {

  /**
   * Handles incoming httprequest.
   * @param exchange the exchange containing data about the request and response.
   * @throws IOException When writing the response fails.
   */
  public void handle(HttpExchange exchange) throws IOException {
    getLogger().log("Got an http request with uri: "
            + exchange.getRequestURI(), LogEvent.Type.INFO);
    respond(exchange, getCameraController().getCamerasJSON());    
  }
}
