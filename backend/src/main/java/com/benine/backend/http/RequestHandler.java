package com.benine.backend.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public abstract class RequestHandler implements HttpHandler {
  /**
   * Decodes the given (decoded) uri into an attributes table
   * @param uri the uri to parse.
   * @return an attributes object containing key->value(string->string)
   * pairs for the uri parameters.
   */
  public Attributes parseURI(String uri) throws MalformedURIException {
    Attributes params = new Attributes();
    for(String pair : uri.split("&")) {
      String[] splitPair = pair.split("=");
      if(params.containsKey(new Attributes.Name(splitPair[0]))) {
        throw new MalformedURIException("Multiple occurences of parameter with name: " + splitPair[0]);
      }
      params.putValue(splitPair[0], splitPair[1]);
    }
    return params;
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
      out.write(response.getBytes());
      out.close();
    } catch (IOException e) {
      // TODO Log exception
    }


  }




}
