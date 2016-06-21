package com.benine.backend.http.presethandlers;

import com.benine.backend.LogEvent;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.preset.Preset;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that handles editing presets.
 */
public class PresetDeletionHandler extends PresetRequestHandler {

  /**
   * Constructor for a new editPresetHandler that handles editing a preset.
   * @param httpserver this handler is for.
   */
  public PresetDeletionHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    boolean success = true;
    
    try {
      int presetID = Integer.parseInt(request.getParameter("id"));
      Preset preset = getPreset(presetID);

      if (preset != null) {
        getPresetController().removePreset(presetID);
      } else {
        success = false;
      }

    } catch (NumberFormatException e) {
      getLogger().log("Invalid parameter input", LogEvent.Type.INFO);
    } catch (SQLException e) {
      getLogger().log("An SQL Exception occured", LogEvent.Type.INFO);
    }

    respond(request, res, success);
    request.setHandled(true);
  }
}
