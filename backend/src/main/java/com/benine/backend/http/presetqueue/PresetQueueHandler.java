package com.benine.backend.http.presetqueue;

import com.benine.backend.http.HTTPServer;
import com.benine.backend.http.RequestHandler;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles the HTTP request for /presetsqueues.
 */
public class PresetQueueHandler extends PresetQueueRequestHandler {
  
  /**
   * Map containing the handlers, (route, handler).
   */
  private Map<String, RequestHandler> handlers;

  /**
   * Constructs a presetQueuehandler for this httpserver.
   * @param httpserver of this preset queue.
   */
  public PresetQueueHandler(HTTPServer httpserver) {
    super(httpserver);
    this.handlers = new HashMap<>();

    addHandler("/create", new CreatePresetQueueHandler(httpserver));
    addHandler("/delete", new DeletePresetQueueHandler(httpserver));
    addHandler("/add", new AddPresetToPresetQueueHandler(httpserver));
    addHandler("/deletepreset", new DeletePresetFromPresetQueueHandler(httpserver));
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
      String presetQueues = getPresetQueueController().getPresetQueueJSON();
      respond(request, res, presetQueues);
      request.setHandled(true);
    }
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
