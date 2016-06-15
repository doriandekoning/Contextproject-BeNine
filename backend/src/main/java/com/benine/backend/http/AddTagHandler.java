package com.benine.backend.http;

import org.eclipse.jetty.server.Request;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by BeNine on 25-5-16.
 */
public class AddTagHandler extends RequestHandler {

  /**
   * Constructs a addtaghandler for the htpserver.
   * @param httpserver for this handler.
   */
  public AddTagHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    String tagName = request.getParameter("name");
    boolean succes = false;
    if (tagName != null) {
      getPresetController().addTag(tagName);
      succes = true;
    }
    
    respond(request, httpServletResponse, succes);
    request.setHandled(true);
  }
}
