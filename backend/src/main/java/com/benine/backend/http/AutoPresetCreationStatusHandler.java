package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.autopresetcreation.AutoPresetCreator;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AutoPresetCreationStatusHandler extends AutoPresetHandler  {

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
    Boolean succes = false;
    String camID = request.getParameter("camera");
    Camera cam = getCameraById(Integer.parseInt(camID));
    if (!(cam instanceof IPCamera )) {
      respond(request, httpServletResponse, succes);
      request.setHandled(true);
      return;
    }
    AutoPresetCreator creator = AutoPresetCreationHandler.getCreators().get(cam.getId());
    if ( creator != null) {
      JSONObject object = new JSONObject();
      JSONArray presets = new JSONArray();
      creator.getGeneratedPresets().forEach(p -> presets.add(p.toJSON()));
      object.put("created", presets);
      object.put("amount_total", creator.getTotalAmountPresets());
      respond(request, httpServletResponse, object.toString());
    } else {
      respond(request, httpServletResponse, succes);
    }
    request.setHandled(true);
  }

}
