package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Mockito.*;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandlerTest {

  @Test
  public void testGetInfo() throws Exception {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "serverControllertest.conf");
    ServerController serverController = ServerController.getInstance();
   
    // Setup mocks
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    ZoomingCamera cam = mock(ZoomingCamera.class);
    
    CameraController camController = new CameraController();
    serverController.setCameraController(camController);

    camController.addCamera(cam);
    // Setup request uri
    URI uri = new  URI("http://localhost/camera/");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    CameraInfoHandler cHandler = new CameraInfoHandler(mock(Logger.class));
    try {
      cHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Test if correct json string is written to the output
    verify(out).write(camController.getCamerasJSON().getBytes());
  }
}
