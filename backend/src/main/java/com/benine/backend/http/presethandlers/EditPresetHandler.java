package com.benine.backend.http.presethandlers;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.http.HTTPServer;
import com.benine.backend.preset.Preset;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that handles editing presets.
 */
public class EditPresetHandler extends PresetRequestHandler {
  
  /**
   * Constructor for a new editPresetHandler that handles editing a preset.
   * @param httpserver this handler is for.
   */
  public EditPresetHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    Boolean succes = true;
    try {
      final String overwriteTag = request.getParameter("overwritetag");
      final String overwritePosition = request.getParameter("overwriteposition");
      int presetID = Integer.parseInt(request.getParameter("presetid"));
      String tags = request.getParameter("tags");
      String name = request.getParameter("name");
      
      Preset preset = getPreset(presetID);
      Set<String> tagList = new HashSet<>();
      if (name != null) {
        preset.setName(name);
      }
      if (tags != null) {
        tagList = new HashSet<>(Arrays.asList(tags.split("\\s*,\\s*"))); 
      }
      if (overwriteTag.equals("true")) {
        updateTag(preset, tagList);
      }
      if (overwritePosition.equals("true")) {
        preset = updatePosition(preset);
        getPresetController().createImage(preset);
      }
      getPresetController().updatePreset(preset);
      
      succes = false;
    } catch (SQLException e) {
      getLogger().log(e.getMessage(), e);
      succes = false;
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera.", e);
      succes = false;
    }  catch (CameraBusyException e) {
      getLogger().log("Camera is busy.", LogEvent.Type.WARNING);
      succes = false;
    } finally {
      respond(request, res, succes);
      request.setHandled(true);
    }
  }
  
  /**
   * Updating the tag only.
   * @param preset the preset to be changed
   * @param tagList the tag to be added
   * @throws SQLException when preset can not be updated.
   */
  private void updateTag(Preset preset, Set<String> tagList) throws SQLException {
    preset.removeTags();
    preset.addTags(tagList);
  }
  
  /**
   * Editing an already existing preset by removing the old preset and creating a new 
   * preset with the same preset and camera id. It also creates a new image that belongs to the 
   * preset and updates the database.
   * @param preset                        The preset to be updated.
   * @return updated preset
   * @throws SQLException                 If the preset cannot be written to the database.
   * @throws CameraConnectionException    If the camera cannot be reached.
   * @throws CameraBusyException          If camera is busy
   */
  private Preset updatePosition(Preset preset) throws
            CameraConnectionException, CameraBusyException, SQLException {
    IPCamera ipcam = (IPCamera) getCameraController().getCameraById(preset.getCameraId());   
    Preset newPreset = ipcam.createPreset(preset.getTags(), preset.getName());
    newPreset.setId(preset.getId());
    return newPreset;
  }
}
