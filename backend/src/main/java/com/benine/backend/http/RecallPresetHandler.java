package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
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
      int cameraID = Integer.parseInt(request.getParameter("currentcamera"));
      int presetID = Integer.parseInt(request.getParameter("presetid"));

      PresetController presetController = ServerController.getInstance().getPresetController();
      Preset preset = presetController.getPresetById(presetID);

      CameraController cameraController = ServerController.getInstance().getCameraController();
      Camera camera = cameraController.getCameraById(cameraID);

      moveCamera(camera, preset);
      respondSuccess(request, res);

    } catch (CameraConnectionException e) {
      e.printStackTrace();
      respondFailure(request, res);
    } catch (MalformedURIException | NumberFormatException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }

    request.setHandled(true);
  }

  /**
   * Moves the camera
   * @param camera  A Camera object.
   * @param preset  The preset to move the camera to.
   * @throws CameraConnectionException  If the camera cannot be reached.
   * @throws MalformedURIException      If the request contains an error.
   */
  public void moveCamera(Camera camera, Preset preset)
          throws CameraConnectionException, MalformedURIException {
    if (camera instanceof IPCamera) {
      IPCamera ipcamera = (IPCamera) camera;

      Position position = preset.getPosition();
      ipcamera.moveTo(position, preset.getPanspeed(), preset.getTiltspeed());
      ipcamera.zoomTo(preset.getZoom());
      ipcamera.moveFocus(preset.getFocus());
      ipcamera.setAutoFocusOn(preset.isAutofocus());
      ipcamera.setIrisPosition(preset.getIris());
      ipcamera.setAutoIrisOn(preset.isAutoiris());
    } else {
      throw new MalformedURIException("Camera cannot be controller over IP");
    }
  }
}
