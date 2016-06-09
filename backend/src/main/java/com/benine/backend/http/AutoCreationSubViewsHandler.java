package com.benine.backend.http;

import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.preset.autopresetcreation.SubView;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


public class AutoCreationSubViewsHandler extends AutoPresetHandler  {


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
    final String rowsString = request.getParameter("rows");
    final String columnsString = request.getParameter("columns");
    final String levelsString = request.getParameter("levels");
    final String overlapString = request.getParameter("overlap");
    int rows = rowsString != null ? Integer.parseInt(rowsString) : 3;
    int columns = columnsString != null ? Integer.parseInt(columnsString) : 3;
    int levels = levelsString != null ? Integer.parseInt(levelsString) : 3;
    double overlap = overlapString != null ? Double.parseDouble(overlapString) : 0;
    PresetPyramidCreator creator =  new PresetPyramidCreator(rows, columns, levels, overlap, getPresetController());
  // PresetPyramidCreator creator = getPyramidPresetCreator(request);
    Collection<SubView> subViews = creator.generateSubViews();
    JSONArray subViewsJSON = new JSONArray();
    subViewsJSON.addAll(subViews);
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("SubViews", subViewsJSON);
    respond(request, httpServletResponse, jsonObj.toJSONString());

    request.setHandled(true);

  }
  
}
