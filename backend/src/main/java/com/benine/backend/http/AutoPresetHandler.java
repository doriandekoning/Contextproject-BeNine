package com.benine.backend.http;

import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import org.eclipse.jetty.server.Request;

/**
 * Handles requests that have to do with auto preset creation.
 */
public abstract class AutoPresetHandler extends RequestHandler {
  /**
   * Constructor for a new PresetsHandler, handling the /presets/ request.
   * @param httpserver to construct this handler for.
   */
  public AutoPresetHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  /**
   * Constructs a PresetCreator from the parameters of an request object.
   * @param request the request to get the data from
   * @return The created PyramidPresetCreator
   */
  public PresetPyramidCreator getPyramidPresetCreator(Request request) {
    final String rowsString = request.getParameter("rows");
    final String columnsString = request.getParameter("columns");
    final String levelsString = request.getParameter("levels");
    final String overlapString = request.getParameter("overlap");
    int rows = rowsString != null ? Integer.parseInt(rowsString) : 3;
    int columns = columnsString != null ? Integer.parseInt(columnsString) : 3;
    int levels = levelsString != null ? Integer.parseInt(levelsString) : 3;
    if (rows < 1 || rows > 5) {
      throw new IllegalArgumentException();
    }
    if (columns < 1 || columns > 5) {
      throw new IllegalArgumentException();
    }
    if (levels < 1 || levels > 4) {
      throw new IllegalArgumentException();
    }
    double overlap = overlapString != null ? Double.parseDouble(overlapString) : 0;
    return new PresetPyramidCreator(rows, columns, levels, overlap, getPresetController());
  }
}
