package com.benine.backend.http;

import com.benine.backend.ServerController;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dorian on 25-5-16.
 */
public class RemoveTagHandler extends RequestHandler {
  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    String tagName = request.getParameter("name");
    if (tagName != null) {
      ServerController.getInstance().getPresetController().removeTag(tagName);
    }
    respondSuccess(request, httpServletResponse);
    request.setHandled(true);

  }
}
