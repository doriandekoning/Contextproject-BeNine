package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import org.eclipse.jetty.server.Request;

/**
 * Handles requests that have to do with auto preset creation.
 */
public abstract class AutoPresetHandler extends RequestHandler {
  
  private CameraController cameraController;
  
  private PresetController presetController;
  
  private Logger logger;
  
  /**
   * Constructor for a new PresetsHandler, handling the /presets/ request.
   * @param httpserver to construct this handler for.
   */
  public AutoPresetHandler(HTTPServer httpserver) {
    super(httpserver);
    this.cameraController = httpserver.getCameraController();
    this.presetController = httpserver.getPresetController();
    this.logger = httpserver.getLogger();
  }
  
  /**
   * Get the right camera.
   * @param camID to find the camera.
   * @return right camera.
   */
  protected Camera getCameraById(int camID) {
    return cameraController.getCameraById(camID);
  }
  
  protected Logger getLogger() {
    return logger;
  }
  
  protected PresetController getPresetController() {
    return presetController;
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
    double overlap = overlapString != null ? Double.parseDouble(overlapString) : 0;
    return new PresetPyramidCreator(rows, columns, levels, overlap, getPresetController());
  }
}
