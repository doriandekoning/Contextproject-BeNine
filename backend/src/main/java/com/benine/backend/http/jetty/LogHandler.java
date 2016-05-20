package com.benine.backend.http.jetty;

import com.benine.backend.LogEvent;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Logs all http requests.
 */
public class LogHandler extends RequestHandler {

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
    getLogger().log("Got an http request with uri: "
            + request.getRequestURI(), LogEvent.Type.INFO);
  }
}
