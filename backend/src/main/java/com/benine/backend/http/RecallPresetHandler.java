package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import com.benine.backend.camera.*;
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
      getLogger().log("Error connectiong to camera", e);
    } catch (CameraBusyException e) {
      getLogger().log("Camera is busy", e);
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
   * @throws CameraBusyException if camera is busy.
   */
  public void moveCamera(Camera camera, Preset preset)
          throws CameraConnectionException, MalformedURIException, CameraBusyException {
    if (camera instanceof IPCamera) {
      IPCamera ipcamera = (IPCamera) camera;

      Position position = preset.getPosition();
      ipcamera.moveTo(position, preset.getPanspeed(), preset.getTiltspeed());
      ipcamera.zoomTo(preset.getZoom());
      ipcamera.setAutoFocusOn(preset.isAutofocus());
      ipcamera.setAutoIrisOn(preset.isAutoiris());
      ipcamera.moveFocus(preset.getFocus());  
      ipcamera.setIrisPosition(preset.getIris());
    } else {
      throw new MalformedURIException("Camera cannot be controller over IP");
    }
  }
}
