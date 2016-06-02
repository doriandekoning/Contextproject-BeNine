package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RecallPresetHandler extends RequestHandler {

  /**
   * Constructor for a new RecallPresetHandler, handling the /presets/recallpreset request.
   */
  public RecallPresetHandler() {}

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    try {
      int presetID = Integer.parseInt(request.getParameter("presetid"));

      PresetController presetController = ServerController.getInstance().getPresetController();
      Preset preset = presetController.getPresetById(presetID);

      CameraController cameraController = ServerController.getInstance().getCameraController();
      Camera camera = cameraController.getCameraById(preset.getCameraId());

      preset.excecutePreset(camera);
      respondSuccess(request, res);

    } catch (CameraConnectionException e) {
      e.printStackTrace();
      respondFailure(request, res);
    } catch (NumberFormatException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }

    request.setHandled(true);
  }
}
