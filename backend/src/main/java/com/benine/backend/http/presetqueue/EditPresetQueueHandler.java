package com.benine.backend.http.presetqueue;

import com.benine.backend.LogEvent;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.performance.PresetQueue;

import org.eclipse.jetty.server.Request;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Edit preset queue handler.
 */
public class EditPresetQueueHandler extends PresetQueueRequestHandler {

  /**
   * Constructs a handler for deleting a preset queue /presetqueues/{id}/edit
   * @param httpserver this handler belongs to.
   */
  public EditPresetQueueHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String arg0, Request request, HttpServletRequest arg2, HttpServletResponse res)
      throws IOException, ServletException {
    int id = getPresetsQueueId(request);
    String name = request.getParameter("name");
    PresetQueue presetQueue = getPresetQueueController().getPresetQueueById(id);
    
    if (presetQueue != null && name != null) {
      presetQueue.setName(name);
      getPresetQueueController().updatePresetQueue(presetQueue);
      respondSuccess(request, res);
      getLogger().log("The name of Preset queue " + id 
                                + " is succesfully updated.", LogEvent.Type.INFO);
    } else {
      respondFailure(request, res);
      getLogger().log("Preset queue " + id + " is not deleted.", LogEvent.Type.WARNING);
    }
    
    request.setHandled(true);
  }

}
