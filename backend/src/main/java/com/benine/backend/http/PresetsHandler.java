package com.benine.backend.http;

import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PresetsHandler extends RequestHandler {

  /**
   * Map containing the handlers, (route, handler);
   */
  private Map<String, RequestHandler> handlers;

  /**
   * Constructor for a new PresetsHandler, handling the /presets/ request.
   */
  public PresetsHandler() {
    this.handlers = new HashMap<>();
    setHandlers(new PresetsCreatePresetHandler(), new PresetsRecallPresetHandler());
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    String route = getRoute(request);

    boolean routed = false;
    
    if (handlers.containsKey(route)) {
      handlers.get(route).handle(s, request, req, res);
      routed = true;
    }

    if (!routed) {
      String presetInfo = getPresetsInfo(request);
      respond(request, res, presetInfo);
      request.setHandled(true);
    }
  }

  /**
   * Returns a JSON string about presets.
   * @param request A request object.
   * @return  JSON string with preset information.
   */
  private String getPresetsInfo(Request request) {
    String tag = request.getParameter("tag");

    ArrayList<Preset> presets;
    PresetController controller = ServerController.getInstance().getPresetController();
    if (tag == null) {
      presets = controller.getPresets();
    } else {
      presets = controller.getPresetsByTag(tag);
    }

    JSONArray json = new JSONArray();
    for (Preset preset : presets) {
      json.add(preset.toJSON());
    }

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("presets", json);
    return jsonObject.toString();
  }

  /**
   * Returns the route of the url, so we can select the next handler.
   *
   * @param request The current request.
   * @return Returns the route.
   */
  private String getRoute(Request request) {
    String path = request.getPathInfo();

    return path.replaceFirst(".*/", "");
  }

  /**
   * Sets the handlers where this handler routes to.
   * @param createPreset  a PresetsCreatePresetHandler.
   * @param recallPreset  a PresetsRecallPresetHandler.
   */
  public void setHandlers(PresetsCreatePresetHandler createPreset,
                          PresetsRecallPresetHandler recallPreset) {

    handlers.put("createpreset", createPreset);
    handlers.put("recallpreset", recallPreset);

  }
}
