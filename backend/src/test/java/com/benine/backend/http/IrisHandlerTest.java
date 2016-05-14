package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.camera.*;
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
public class IrisHandlerTest {

  @Test
  public void testOnlyAutoIris() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    IrisCamera cam = mock(IrisCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?autoIrisOn=true");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    IrisHandler fHandler = new IrisHandler(camController, mock(Logger.class));
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setAutoIrisOn(true);
  }


  @Test
  public void testOnlyIrisPosIris() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    IrisCamera cam = mock(IrisCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/iris?position=3");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    IrisHandler fHandler = new IrisHandler(camController, mock(Logger.class));
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setIrisPosition(3);
  }


  @Test
  public void testAutoIrisOnAndIrisPosIris() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    IrisCamera cam = mock(IrisCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/camera/" +cam.getId()+ "/iris?position=3&autoIrisOn=true");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    IrisHandler fHandler = new IrisHandler(camController, mock(Logger.class));
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setIrisPosition(3);
    verify(cam).setAutoIrisOn(true);
  }
}
