package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.FocussingCamera;
import com.sun.net.httpserver.HttpExchange;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

/**
 * Created by dorian on 4-5-16.
 */
public class FocussingHandlerTest {
  
  HttpExchange exchange = mock(HttpExchange.class);
  OutputStream out = mock(OutputStream.class);
  FocussingCamera cam = mock(FocussingCamera.class);
  ServerController serverController = mock(ServerController.class);
  FocussingHandler fHandler;
  
  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    ServerController serverController = ServerController.getInstance();
    
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    serverController.setCameraController(camController);
    fHandler = new FocussingHandler(mock(Logger.class));
    when(exchange.getResponseBody()).thenReturn(out);
  }

  @Test
  public void testOnlyAutoFocus() throws Exception {  
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/focus?autoFocusOn=true");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setAutoFocusOn(true);
  }

  @Test
  public void testMoveFocusRelative() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/focus?speed=5");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).moveFocus(5);
  }


  @Test
  public void testOnlyPosition() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/focus?position=3");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setFocusPosition(3);
  }


  @Test
  public void testPositionAndAutoFocus() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/focus?position=3&autoFocusOn=false");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setFocusPosition(3);
    verify(cam).setAutoFocusOn(false);
  }
  
  @Test
  public void testMalformedURI() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/focus?position=4&position=3&autoFocusOn=false");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }
  
  @Test
  public void testCameraConnectionException() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/focus?position=4&autoFocusOn=false");
    when(exchange.getRequestURI()).thenReturn(uri);
    doThrow(new CameraConnectionException("test exception", 0)).when(cam).setFocusPosition(4);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }

}
