package com.benine.backend.http;

import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.preset.autopresetcreation.SubView;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AutoCreationSubViewsHandler extends RequestHandler  {


  /**
   * Constructs a request handler.
   *
   * @param httpserver to interact with the rest of the system.
   */
  public AutoCreationSubViewsHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    PresetPyramidCreator creator = new PresetPyramidCreator(2,2,3,0.1, getPresetController());
    Collection<SubView> subViews = creator.generateSubViews();
    JSONArray subViewsJSON = new JSONArray();
    subViewsJSON.addAll(subViews);
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("SubViews", subViewsJSON);
    respond(request, httpServletResponse, jsonObj.toJSONString());

  }
  
}
