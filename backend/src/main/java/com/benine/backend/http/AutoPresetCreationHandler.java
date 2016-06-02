package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.PresetPyramidCreator;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AutoPresetCreationHandler extends RequestHandler  {

  
  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    
    PresetPyramidCreator creator = new PresetPyramidCreator(3,3,3,0.1);
    
    String camID = request.getParameter("camera"); 
    IPCamera camera = (IPCamera)ServerController.getInstance().getCameraController()
        .getCameraById(Integer.parseInt(camID));
   
    try {
      ArrayList<Preset> presets = new ArrayList<Preset>(creator.createPresets(camera));
      PresetController presetController = ServerController.getInstance().getPresetController();
      presetController.addPresets(presets);
    } catch (CameraConnectionException | InterruptedException | TimeoutException | SQLException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
    }

   

  }
  
}
