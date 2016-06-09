package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.preset.autopresetcreation.SubView;
import com.benine.backend.video.StreamNotAvailableException;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoPresetCreationHandler extends RequestHandler  {


  /**
   * Constructs a request handler.
   *
   * @param httpserver to interact with the rest of the system.
   */
  public AutoPresetCreationHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    PresetPyramidCreator creator = new PresetPyramidCreator(2,2,2,0.1, getPresetController());
    String camID = request.getParameter("camera");
    Camera cam = ServerController.getInstance().getCameraController()
        .getCameraById(Integer.parseInt(camID));
    if (!(cam instanceof IPCamera )) {
      System.out.println(" not a ipcam");
      respondFailure(request, httpServletResponse);
      return;
    }
    IPCamera ipcam = (IPCamera) cam;
   
    try {
      Collection<SubView> subViews = creator.generateSubViews();
      creator.createPresets(ipcam, subViews);
      JSONArray subViewsJSON = new JSONArray();
      subViewsJSON.addAll(subViews);
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("SubViews", subViewsJSON);
      respond(request, httpServletResponse, jsonObj.toJSONString());

    } catch (CameraConnectionException | InterruptedException
            | TimeoutException | StreamNotAvailableException | SQLException e ) {
      getLogger().log("Exception occured while trying to auto create presets", e);
      respondFailure(request, httpServletResponse);
    }  catch (CameraBusyException e) {
      getLogger().log("Trying to auto create presets on busy camera with id: "
              + camID, LogEvent.Type.WARNING);
      respondFailure(request, httpServletResponse);
    }


  }
  
}
