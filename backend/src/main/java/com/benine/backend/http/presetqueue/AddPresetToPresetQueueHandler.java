package com.benine.backend.http.presetqueue;

import com.benine.backend.LogEvent;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.http.MalformedURIException;
import com.benine.backend.performance.PresetQueue;
import com.benine.backend.preset.Preset;

import org.eclipse.jetty.server.Request;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Add preset to preset queue handler.
 */
public class AddPresetToPresetQueueHandler extends PresetQueueRequestHandler {

  /**
   * Constructs a handler for adding a preset to a preset queue /presetqueues/{id}/add
   * @param httpserver this handler belongs to.
   */
  public AddPresetToPresetQueueHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String arg0, Request request, HttpServletRequest arg2, HttpServletResponse res)
      throws IOException, ServletException {
    int id = getPresetsQueueId(request);
    int presetid = Integer.parseInt(request.getParameter("presetid"));
   
    Preset preset = getPresetController().getPresetById(presetid);   

    PresetQueue presetQueue = getPresetQueueController().getPresetQueueById(id);
    
    if (presetQueue != null && preset != null) {
      if (request.getParameter("positie") == null) {
        presetQueue.addPresetEnd(preset);
      } else {
        presetQueue.insertPreset(Integer.parseInt(request.getParameter("positie")), preset);
      }
      
      respondSuccess(request, res);
      getLogger().log("Preset " + presetid + " is succesfully added to queue: " 
                                                          + id, LogEvent.Type.INFO);
    } else {
      respondFailure(request, res);
      getLogger().log("Preset " + presetid + " is not added to queue: " 
                                                          + id, LogEvent.Type.WARNING);
    }
    
    request.setHandled(true);
  }
 

}
