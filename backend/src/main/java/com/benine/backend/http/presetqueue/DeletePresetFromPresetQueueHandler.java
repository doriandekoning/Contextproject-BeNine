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
 * Delete a preset from the preset queue handler.
 */
public class DeletePresetFromPresetQueueHandler extends PresetQueueRequestHandler {

  /**
   * Constructs a handler for deleting a preset to a preset queue /presetqueues/{id}/deletepreset
   * @param httpserver this handler belongs to.
   */
  public DeletePresetFromPresetQueueHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String arg0, Request request, HttpServletRequest arg2, HttpServletResponse res)
      throws IOException, ServletException {
    int id = getPresetsQueueId(request);
    int place = -1;
    
    PresetQueue presetQueue = getPresetQueueController().getPresetQueueById(id);
    
    if (presetQueue != null && request.getParameter("position") != null) {
      place = Integer.parseInt(request.getParameter("position"));
      presetQueue.deletePreset(place);
      getPresetQueueController().updatePresetQueue(presetQueue);
      
      respondSuccess(request, res);
      getLogger().log("Preset at position " + place + " is succesfully deleted from the queue: " 
                                                          + id, LogEvent.Type.INFO);
    } else {
      respondFailure(request, res);
      getLogger().log("Preset at position " + place + " is not deleted from the queue: " 
                                                          + id, LogEvent.Type.WARNING);
    }
    
    request.setHandled(true);
  }
 

}
