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
 * Delete preset queue handler.
 */
public class DeletePresetQueueHandler extends PresetQueueRequestHandler {

  /**
   * Constructs a handler for deleting a preset queue /presetqueues/{id}/delete
   * @param httpserver this handler belongs to.
   */
  public DeletePresetQueueHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String arg0, Request request, HttpServletRequest arg2, HttpServletResponse res)
      throws IOException, ServletException {
    int id = getPresetsQueueId(request);

    PresetQueue presetQueue = getPresetQueueController().getPresetQueueById(id);
    boolean succes = false;
    if (presetQueue != null) {
      getPresetQueueController().removePresetQueue(presetQueue);
      succes = true;
      getLogger().log("Preset queue " + id + "is succesfully deleted.", LogEvent.Type.INFO);
    } else {
      getLogger().log("Preset queue " + id + "is not deleted.", LogEvent.Type.WARNING);
    }
    
    respond(request, res, succes);
    request.setHandled(true);
  }

}
