package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.autopresetcreation.AutoPresetCreator;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.video.StreamNotAvailableException;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


public class AutoPresetCreationStatusHandler extends AutoPresetHandler  {


  private static HashMap<Integer, AutoPresetCreator> creators = new HashMap<>();

  /**
   * Constructs a request handler.
   * @param httpserver to interact with the rest of the system.
   */
  public AutoPresetCreationStatusHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    String camID = request.getParameter("camera");
    Camera cam = getCameraController().getCameraById(Integer.parseInt(camID));
    if (!(cam instanceof IPCamera )) {
      respondFailure(request, httpServletResponse);
      request.setHandled(true);
      return;
    }
    AutoPresetCreator creator = AutoPresetCreationHandler.getCreators().get(cam.getId());
    if ( creator != null) {
      JSONObject object = new JSONObject();
      object.put("amount-created", creator.getGeneratedPresetsAmount());
      respond(request, httpServletResponse, object.toString());
      creators.remove(cam.getId());
    } else {
      respondFailure(request, httpServletResponse);
    }
    request.setHandled(true);

  }


  /**
   * Returns the auto preset creators currently running.
   */
  public Map<Integer, AutoPresetCreator> getCreators() {
    return creators;
  }
}
