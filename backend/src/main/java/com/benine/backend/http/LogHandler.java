package com.benine.backend.http;

import com.benine.backend.LogEvent;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Logs all http requests that come in.
 */
public class LogHandler extends RequestHandler {

  /**
   * Constructs the log handler for this server.
   * @param httpserver for which the handler is created.
   */
  public LogHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    getLogger().log("Got an http request with uri: "
            + request.getRequestURI(), LogEvent.Type.INFO);
  }
}
