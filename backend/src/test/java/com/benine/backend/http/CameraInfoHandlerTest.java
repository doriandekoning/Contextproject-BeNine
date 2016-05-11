package com.benine.backend.http;

import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;

import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Mockito.*;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandlerTest {

  @Test
  public void testGetInfo() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    ZoomingCamera cam = mock(ZoomingCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/camera");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    ZoomingHandler zHandler = new ZoomingHandler(camController, 0);
    try {
      zHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).zoomTo(2);
  }

  @Test
  public void testZoomRelative() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    ZoomingCamera cam = mock(ZoomingCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/zoom?id=0&zoomType=relative&zoom=2");
    System.out.println(uri.getQuery());
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    ZoomingHandler zHandler = new ZoomingHandler(camController, 0);
    try {
      zHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).zoom(2);
  }

}
