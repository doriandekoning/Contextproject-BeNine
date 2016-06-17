package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.autopresetcreation.AutoPresetCreator;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.video.StreamNotAvailableException;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AutoPresetCreationHandler extends AutoPresetHandler  {


  private static ConcurrentHashMap<Integer, AutoPresetCreator> creators = new ConcurrentHashMap<>();

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
    creators.put(cam.getId(), creator);
    IPCamera ipcam = (IPCamera) cam;
   
    try {
      ArrayList<IPCameraPreset> presets =
              new ArrayList<>(creator.createPresets(ipcam, creator.generateSubViews()));

      String name = request.getParameter("name");
      JSONObject jsonObject = new JSONObject();
      JSONArray idsJson = new JSONArray();
      for (int i = 0; i < presets.size(); i++) {
        presets.get(i).setName(name + " " + i);
        getPresetController().updatePreset(presets.get(i));
      }
      
      presets.forEach(preset -> idsJson.add(preset.getId()));
      jsonObject.put("presetIDs", idsJson);
      respond(request, httpServletResponse, jsonObject.toJSONString());
    } catch (CameraConnectionException | InterruptedException
            | TimeoutException | StreamNotAvailableException | SQLException e ) {
      getLogger().log("Exception occured while trying to auto create presets", e);
      respond(request, httpServletResponse, succes);
    }  catch (CameraBusyException e) {
      getLogger().log("Trying to auto create presets on busy camera with id: "
              + camID, e);
      respond(request, httpServletResponse, succes);
    } finally {    
      creators.remove(cam.getId());
      request.setHandled(true);
    }
  }

  /**
   * Returns the auto preset creators currently running.
   * @return a concurrenthashmap with all the creators currently running.
   */
  public static ConcurrentMap<Integer, AutoPresetCreator> getCreators() {
    return creators;
  }
}
