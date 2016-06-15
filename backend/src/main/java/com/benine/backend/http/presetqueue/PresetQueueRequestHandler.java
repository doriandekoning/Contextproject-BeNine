package com.benine.backend.http.presetqueue;

import com.benine.backend.Logger;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.http.RequestHandler;
import com.benine.backend.performance.PresetQueueController;

import org.eclipse.jetty.server.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles all request requiring the presetsQueue ID.
 */
public abstract class PresetQueueRequestHandler extends RequestHandler {

  /**
   * PresetQueueRequest handler for the httpserver.
   * @param httpserver to construct the PresetQueuHandler for.
   */
  public PresetQueueRequestHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  /**
   * Fetches presetsQueue id from http exchange.
   * @param request the request to fix the id from.
   * @return the id of the preset Queue.
   */
  public int getPresetsQueueId(Request request) {
    Pattern pattern = Pattern.compile("^/(\\d*)/.*");
    String path = request.getPathInfo();

    Matcher m = pattern.matcher(path);

    return m.matches() ? Integer.parseInt(m.group(1)) : -1;
  }

  /**
   * Returns the route of the url, so we can select the next handler.
   * @param request   The current request.
   * @return          Returns the route.
   */
  public String getRoute(Request request) {
    String path = request.getPathInfo();

    return path.replaceFirst(".*/(\\d*)/", "");
  }
  
  @Override
  protected Logger getLogger() {
    return super.getLogger();
  }
  
  @Override
  protected PresetQueueController getPresetQueueController() {
    return super.getPresetQueueController();
  }

}
