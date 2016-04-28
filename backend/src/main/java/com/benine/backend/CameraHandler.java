package com.benine.backend;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Created by dorian on 28-4-16.
 */
public class GetCameraHandler implements HttpHandler {
  public void handle(HttpExchange ext) throws IOException {
    System.out.println(ext.getRequestURI());
  }
}
