package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.FocussingCamera;
import com.sun.net.httpserver.HttpExchange;

import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    CameraController camController = new CameraController(serverController);
    camController.addCamera(cam);
    when(serverController.getCameraController()).thenReturn(camController);
    fHandler = new FocussingHandler(serverController,  mock(Logger.class));
    when(exchange.getResponseBody()).thenReturn(out);
  }

  @Test
  public void testOnlyAutoFocus() throws Exception {  
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?autoFocusOn=true");  
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setAutoFocusOn(true);
  }

  @Test
  public void testOnlyPosition() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?position=3");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setFocusPos(3);
  }


  @Test
  public void testPositionAndAutoFocus() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?position=3&autoFocusOn=false");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setFocusPos(3);
    verify(cam).setAutoFocusOn(false);
  }

}
