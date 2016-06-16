package com.benine.backend.http.presetqueue;

import com.benine.backend.LogEvent;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.performance.PresetQueue;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create preset queue handler.
 */
public class CreatePresetQueueHandler extends PresetQueueRequestHandler {

  /**
   * Constructs a handler for creating a preset queue /presetqueues/create
   * @param httpserver this handler belongs to.
   */
  public CreatePresetQueueHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String arg0, Request request, HttpServletRequest arg2, HttpServletResponse res)
      throws IOException, ServletException {
    String name = request.getParameter("name");
    boolean succes = false;
    if (name != null) {
      PresetQueue presetQueue = new PresetQueue(name, new ArrayList<>());
    
      getPresetQueueController().addPresetQueue(presetQueue);
    
      succes = true;
      getLogger().log("New preset Queue created", LogEvent.Type.INFO);
    } else {
      getLogger().log("New preset queue can't be created", LogEvent.Type.WARNING);
    }
    respond(request, res, succes);
    request.setHandled(true);
  }

}
