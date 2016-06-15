package com.benine.backend.http.presethandlers;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.PresetCamera;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.http.MalformedURIException;
import com.benine.backend.preset.Preset;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler to create presets.
 */
public class CreatePresetHandler extends PresetRequestHandler {

  /**
   * Constructs a create preset handler.
   * @param httpserver this handler belongs to.
   */
  public CreatePresetHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    Boolean succes = true;
    try {
      String camID = request.getParameter("camera");
      if (camID == null) {
        throw new MalformedURIException("No Camera ID Specified.");
      }

      String tags = request.getParameter("tags");
      String name = request.getParameter("name");
      if (name == null) {
        throw new MalformedURIException("No Name Specified.");
      }

      Set<String> tagList = new HashSet<>();
      if (tags != null) {
        tagList = new HashSet<>(Arrays.asList(tags.split("\\s*,\\s*"))); 
      } 
      
      PresetCamera presetCamera = getPresetCamera(camID);
      
      if (presetCamera != null) {
        Preset preset = presetCamera.createPreset(tagList, name);
        getPresetController().addPreset(preset);
        getPresetController().createImage(preset);
      } else {
        throw new MalformedURIException("Camera does not support presets or is nonexistent.");
      }
    } catch (MalformedURIException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      succes = false;
    } catch (SQLException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      succes = false;
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera.", LogEvent.Type.CRITICAL);
      succes = false;
    } catch (CameraBusyException e) {
      getLogger().log("Camera is busy.", LogEvent.Type.WARNING);
      succes = false;
    } finally {
      respond(request, res, succes);
      request.setHandled(true);
    }
  }
}
