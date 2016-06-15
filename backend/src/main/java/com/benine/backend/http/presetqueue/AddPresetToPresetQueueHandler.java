package com.benine.backend.http.presetqueue;

import com.benine.backend.LogEvent;
import com.benine.backend.http.HTTPServer;
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
   * Constructs a handler for adding a preset to a preset queue /presetqueues/{id}/addpreset
   * @param httpserver this handler belongs to.
   */
  public AddPresetToPresetQueueHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String arg0, Request request, HttpServletRequest arg2, HttpServletResponse res)
      throws IOException, ServletException {
    int id = getPresetsQueueId(request);
    int presetid = -1;
    String position = request.getParameter("position");
    PresetQueue presetQueue = getPresetQueueController().getPresetQueueById(id);
    boolean correct = checkInput(request, presetQueue);
    boolean succes = false;
    if (correct) {
      presetid = Integer.parseInt(request.getParameter("presetid"));
      Preset preset = getPresetController().getPresetById(presetid);   
      getPresetQueueController().updatePresetQueue(addPreset(position, preset, presetQueue));
      succes = true;
      getLogger().log("Preset " + presetid + " is succesfully added to queue: " 
                                                          + id, LogEvent.Type.INFO);
    } else {
      getLogger().log("Preset " + presetid + " is not added to queue: " 
                                                          + id, LogEvent.Type.WARNING);
    }
    
    respond(request, res, succes);
    request.setHandled(true);
  }
  
  /**
   * Adds the preset add the position specified.
   * If no position specified it will be added to the end.
   * @param position to add the preset to.
   * @param preset to add to the queue.
   * @param presetQueue where the preset will be added to.
   * @return updated presetQueue
   */
  private PresetQueue addPreset(String position, Preset preset, PresetQueue presetQueue) {
    if (position == null) {
      presetQueue.addPresetEnd(preset);
    } else {
      presetQueue.insertPreset(Integer.parseInt(position), preset);
    }
    return presetQueue;
  }
  
  /**
   * Check if the input is valid.
   * @param request to check.
   * @param presetQueue this request if for.
   * @return true if there is no failure.
   */
  private Boolean checkInput(Request request, PresetQueue presetQueue) {
    if (presetQueue == null || request.getParameter("presetid") == null) {
      return false;
    }
    int presetid = Integer.parseInt(request.getParameter("presetid"));
    if (getPresetController().getPresetById(presetid) == null) {
      return false;
    }
    return true;
  }
 

}
