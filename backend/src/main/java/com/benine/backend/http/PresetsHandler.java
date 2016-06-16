package com.benine.backend.http;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class PresetsHandler extends RequestHandler {

  /**
   * Map containing the handlers, (route, handler).
   */
  private Map<String, RequestHandler> handlers;

  /**
   * Constructor for a new PresetsHandler, handling the /presets/ request.
   * @param httpserver to construct this handler for.
   */
  public PresetsHandler(HTTPServer httpserver) {
    super(httpserver);
    this.handlers = new HashMap<>();


    addHandler("createpreset", new CreatePresetHandler(httpserver));
    addHandler("recallpreset", new RecallPresetHandler(httpserver));
    addHandler("deletepreset", new PresetDeletionHandler(httpserver));
    addHandler("addtag", new AddTagHandler(httpserver));
    addHandler("removetag", new RemoveTagHandler(httpserver));
    addHandler("edit", new EditPresetHandler(httpserver));
    addHandler("autocreatepresets", new AutoPresetCreationHandler(httpserver));
    addHandler("autocreatesubviews", new AutoCreationSubViewsHandler(httpserver));
    addHandler("autocreatepresetsstatus", new AutoPresetCreationStatusHandler(httpserver));
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    String route = getRoute(request);

    boolean routed = false;

    if (handlers.containsKey(route)) {
      handlers.get(route).handle(s, request, req, res);
      routed = true;
    }

    if (!routed) {
      String tag = request.getParameter("tag");
      String presetInfo = getPresetController().getPresetsJSON(tag);
      respond(request, res, presetInfo);
      request.setHandled(true);
    }
  }
  
  /**
   * Returns the route of the url, so we can select the next handler.
   *
   * @param request The current request.
   * @return Returns the route.
   */
  private String getRoute(Request request) {
    String path = request.getPathInfo();
    return path.replaceFirst(".*/", "");
  }

  /**
   * Adds a handler.
   * @param route The route use this handler for.
   * @param handler the handler to add.
   */
  public void addHandler(String route, RequestHandler handler) {
    handlers.put(route, handler);
  }

}
