package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.PresetCamera;
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
public class CreatePresetHandler extends RequestHandler {

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
    try {
      String camID = request.getParameter("camera");
      if (camID == null) {
        throw new MalformedURIException("No Camera ID Specified.");
      }

      Camera camera = getCameraController().getCameraById(Integer.parseInt(camID));
      String tags = request.getParameter("tags");
      String name = request.getParameter("name");
      if (name == null) {
        throw new MalformedURIException("No Name Specified.");
      }

      Set<String> tagList = new HashSet<>();
      if (tags != null) {
        tagList = new HashSet<>(Arrays.asList(tags.split("\\s*,\\s*"))); 
      } 
      if (camera instanceof PresetCamera) {
        PresetCamera presetCamera = (PresetCamera) camera;
        Preset preset = presetCamera.createPreset(tagList, name);
        getPresetController().addPreset(preset);
        getPresetController().createImage(preset);
        respondSuccess(request, res);
      } else {
        throw new MalformedURIException("Camera does not support presets or is nonexistent.");
      }
    } catch (MalformedURIException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (SQLException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera.", LogEvent.Type.CRITICAL);
      respondFailure(request, res);
    } catch (CameraBusyException e) {
      getLogger().log("Camera is busy.", LogEvent.Type.WARNING);
      respondFailure(request, res);
    } finally {
      request.setHandled(true);
    }
  }
}
