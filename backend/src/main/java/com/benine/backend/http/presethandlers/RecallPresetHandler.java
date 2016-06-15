package com.benine.backend.http.presethandlers;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.http.HTTPServer;

import com.benine.backend.preset.Preset;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class RecallPresetHandler extends PresetRequestHandler {

  /**
   * Constructor for a new RecallPresetHandler, handling the /presets/recallpreset request.
   * @param httpserver this handler belongs to.
   */
  public RecallPresetHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    boolean succes = false;
    
    try {
      int presetID = Integer.parseInt(request.getParameter("presetid"));

      Preset preset = getPreset(presetID);
      
      Camera camera = getCameraController().getCameraById(preset.getCameraId());

      preset.excecutePreset(camera);
      succes = true;

    } catch (CameraConnectionException e) {
      getLogger().log("Error connectiong to camera", e);
    } catch (CameraBusyException e) {
      getLogger().log("Camera is busy", e);
    } catch (NumberFormatException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
    }
    
    respond(request, res, succes);
    request.setHandled(true);
  }

}
