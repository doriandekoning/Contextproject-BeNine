package com.benine.backend.http;

import com.benine.backend.ServerController;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by BeNine on 25-5-16.
 */
public class AddTagHandler extends RequestHandler {

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    String tagName = request.getParameter("name");
    if(tagName != null) {
      ServerController.getInstance().getPresetController().addTag(tagName);
    }
    respondSuccess(request, httpServletResponse);
    request.setHandled(true);

  }
}
