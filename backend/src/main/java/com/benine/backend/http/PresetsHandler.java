package com.benine.backend.http;

import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PresetsHandler extends RequestHandler {

  private PresetsCreatePresetHandler createPreset;

  private PresetsRecallPresetHandler recallPreset;

  /**
   * Constructor for a new PresetsHandler, handling the /presets/ request.
   */
  public PresetsHandler() {
    this.createPreset = new PresetsCreatePresetHandler();
    this.recallPreset = new PresetsRecallPresetHandler();
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    String route = getRoute(request);

    boolean routed = false;

    switch (route) {
      case "createpreset":
        createPreset.handle(s, request, req, res);
        routed = true;
        break;
      case "recallpreset":
        recallPreset.handle(s, request, req, res);
        routed = true;
        break;
    }

    if (!routed) {
      String presetInfo = getPresetsInfo(request);
      respond(request, res, presetInfo);
      request.setHandled(true);
    }
  }

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
  public String getRoute(Request request) {
    String path = request.getPathInfo();

    return path.replaceFirst(".*/", "");
  }

  public void setHandlers(PresetsCreatePresetHandler createPreset,
                          PresetsRecallPresetHandler recallPreset) {

    this.createPreset = createPreset;
    this.recallPreset = recallPreset;

  }
}
