package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.video.StreamNotAvailableException;

import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AutoPresetCreationHandler extends AutoPresetHandler  {


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
    Camera cam = getCameraById(Integer.parseInt(camID));
    boolean succes = false;
    
    if (!(cam instanceof IPCamera )) {
      request.setHandled(true);
      respond(request, httpServletResponse, succes);
      return;
    }
    IPCamera ipcam = (IPCamera) cam;
   
    try {
      Collection<IPCameraPreset> presets = creator.createPresets(ipcam, creator.generateSubViews());
      
      JSONObject jsonObject = new JSONObject();
      JSONArray idsJson = new JSONArray();
      
      presets.forEach(preset -> idsJson.add(preset.getId()));
      jsonObject.put("presetIDs", idsJson);
      respond(request, httpServletResponse, jsonObject.toJSONString());
    } catch (CameraConnectionException | InterruptedException
            | TimeoutException | StreamNotAvailableException | SQLException e ) {
      getLogger().log("Exception occured while trying to auto create presets", e);
    }  catch (CameraBusyException e) {
      getLogger().log("Trying to auto create presets on busy camera with id: "
              + camID, e);
    } finally {
      respond(request, httpServletResponse, succes);
      request.setHandled(true);
    }
  }
}
