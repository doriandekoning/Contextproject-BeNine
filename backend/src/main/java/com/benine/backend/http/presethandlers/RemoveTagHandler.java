package com.benine.backend.http.presethandlers;

import com.benine.backend.http.HTTPServer;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 25-5-16.
 */
public class RemoveTagHandler extends PresetRequestHandler {
  
  /**
   * Constructs a the RemoveTaghandler for the httpserver /presets/removetag.
   * @param httpserver to construct the removeTaghandler for.
   */
  public RemoveTagHandler(HTTPServer httpserver) {
    super(httpserver);
  }
  
  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    String tagName = request.getParameter("name");
    Boolean succes = false;
    if (tagName != null) {
      getPresetController().removeTag(tagName);
      succes = true;
    }
    respond(request, httpServletResponse, succes);
    request.setHandled(true);

  }
}
