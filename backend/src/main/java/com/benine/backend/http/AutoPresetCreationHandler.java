package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.autopresetcreation.AutoPresetCreator;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.video.StreamNotAvailableException;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AutoPresetCreationHandler extends AutoPresetHandler  {


  private static HashMap<Integer, AutoPresetCreator> creators = new HashMap<>();

  /**
   * Constructs a request handler.
   * @param httpserver to interact with the rest of the system.
   */
  public AutoPresetCreationHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    PresetPyramidCreator creator = getPyramidPresetCreator(request);
    String camID = request.getParameter("camera");
    Camera cam = getCameraController().getCameraById(Integer.parseInt(camID));
    if (!(cam instanceof IPCamera )) {
      respondFailure(request, httpServletResponse);
      request.setHandled(true);
      return;
    }
    creators.put(cam.getId(), creator);
    IPCamera ipcam = (IPCamera) cam;
   
    try {
      creator.createPresets(ipcam, creator.generateSubViews());
      respondSuccess(request, httpServletResponse);

    } catch (CameraConnectionException | InterruptedException
            | TimeoutException | StreamNotAvailableException | SQLException e ) {
      getLogger().log("Exception occured while trying to auto create presets", e);
      respondFailure(request, httpServletResponse);
    }  catch (CameraBusyException e) {
      getLogger().log("Trying to auto create presets on busy camera with id: "
              + camID, e);
      respondFailure(request, httpServletResponse);
    } finally {
      creators.remove(cam.getId());
      request.setHandled(true);
    }


  }


  /**
   * Returns the auto preset creators currently running.
   */
  public Map<Integer, AutoPresetCreator> getCreators() {
    return creators;
  }
}
