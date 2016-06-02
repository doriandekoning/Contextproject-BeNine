package com.benine.backend.http;

import com.benine.backend.Preset;
import com.benine.backend.PresetPyramidCreator;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.util.Collection;
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
      Collection<Preset> preset = creator.createPresets(camera);
    } catch (CameraConnectionException | InterruptedException | TimeoutException e) {
      e.printStackTrace();
    }

   

  }
  
}
