package com.benine.backend.http;

import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




public class EditPresetHandler extends RequestHandler {
  
  /**
   * Constructor for a new editPresetHandler that handles editing a preset.
   */
  public EditPresetHandler() {}
 

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    
    Boolean overwrite = false;
    
    String tags = request.getParameter("tags");
    List<String> tagList = null;
    if (tags != null) {
    tagList = Arrays.asList(tags.split("\\s*,\\s*")); 
    } else {
      tagList = Arrays.asList("none");
    }
    
    int presetID = Integer.parseInt(request.getParameter("presetid"));
    PresetController presetController = ServerController.getInstance().getPresetController();
    Preset preset = presetController.getPresetById(presetID);
    
    if (overwrite == false) {
      updateTag(preset, tagList);
    }

    request.setHandled(true);
    
  
  }
  
  /**
   * Updating the tag only.
   * @param preset the preset to be changed
   * @param tagList the tag to be added
   */
  public void updateTag(Preset preset, List<String> tagList) {
    preset.removeTags();
    preset.addTags(tagList);
  }
}
