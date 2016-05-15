package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpExchange;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraZoomHandlerTest {
  
  HttpExchange exchange = mock(HttpExchange.class);
  OutputStream out = mock(OutputStream.class);
  ZoomingCamera cam = mock(ZoomingCamera.class);
  ServerController serverController = mock(ServerController.class);
  CameraController camController;
  ZoomingHandler zHandler;
  
  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "serverControllertest.conf");
    ServerController serverController = ServerController.getInstance();
    
    CameraController camController = new CameraController();
    serverController.setCameraController(camController);
    camController.addCamera(cam);
    when(exchange.getResponseBody()).thenReturn(out);
    zHandler = new ZoomingHandler(mock(Logger.class));
  }

  @Test
  public void testZoomAbsolute() throws Exception {  
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?zoomType=absolute&zoom=2");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      zHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).zoomTo(2);
  }

  @Test
  public void testZoomRelative() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?zoomType=relative&zoom=2");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      zHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).zoom(2);
  }
  
  @Test
  public void testMalformedURI() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+ "/zoom?soom=5");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      zHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }

  
 
}
