package com.benine.backend.http;

import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import org.eclipse.jetty.server.Request;

/**
 *
 */
public abstract class AutoPresetHandler extends RequestHandler {
  /**
   * Constructor for a new PresetsHandler, handling the /presets/ request.
   *
   * @param httpserver to construct this handler for.
   */
  public AutoPresetHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  /**
   * Constructs a PresetCreator from the parameters of an request object.
   */
  public PresetPyramidCreator getPyramidPresetCreator(Request request) {
    final String rowsString = request.getParameter("rows");
    final String columnsString = request.getParameter("columns");
    final String levelsString = request.getParameter("levels");
    final String overlapString = request.getParameter("overlap");
    int rows = rowsString != null ? Integer.parseInt(rowsString) : 3;
    int columns = columnsString != null ? Integer.parseInt(columnsString) : 3;
    int levels = levelsString != null ? Integer.parseInt(levelsString) : 3;
    double overlap = overlapString != null ? Double.parseDouble(overlapString) : 0;
    return new PresetPyramidCreator(rows, columns, levels, overlap, getPresetController());
  }
}
