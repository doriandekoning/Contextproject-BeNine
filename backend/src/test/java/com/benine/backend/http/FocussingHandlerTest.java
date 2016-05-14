package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.FocussingCamera;
import com.sun.net.httpserver.HttpExchange;
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

  @Test
  public void testOnlyAutoFocus() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    FocussingCamera cam = mock(FocussingCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?autoFocusOn=true");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    FocussingHandler fHandler = new FocussingHandler(camController,  mock(Logger.class));
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setAutoFocusOn(true);
  }

  @Test
  public void testOnlyPosition() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    FocussingCamera cam = mock(FocussingCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?position=3");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    FocussingHandler fHandler = new FocussingHandler(camController, mock(Logger.class));
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setFocusPosition(3);
  }


  @Test
  public void testPositionAndAutoFocus() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    FocussingCamera cam = mock(FocussingCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?position=3&autoFocusOn=false");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    FocussingHandler fHandler = new FocussingHandler(camController, mock(Logger.class));
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setFocusPosition(3);
    verify(cam).setAutoFocusOn(false);
  }

}
