package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.*;
import com.sun.net.httpserver.HttpExchange;

import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dorian on 4-5-16.
 */
public class MovingHandlerTest {
  
  HttpExchange exchange = mock(HttpExchange.class);
  OutputStream out = mock(OutputStream.class);
  MovingCamera cam = mock(MovingCamera.class);
  ServerController serverController = mock(ServerController.class);
  CameraController camController;
  MovingHandler mHandler = new MovingHandler(serverController, mock(Logger.class));
  
  @Before
  public void setup() {
    camController = new CameraController(serverController);
    when(serverController.getCameraController()).thenReturn(camController);
    camController.addCamera(cam);
    when(exchange.getResponseBody()).thenReturn(out);
    mHandler = new MovingHandler(serverController, mock(Logger.class));
  }

  @Test
  public void testMoveAbsolute() throws Exception {
    URI uri = new  URI("http://localhost/camera/" +cam.getId()+ "/move?pan=1&tilt=2&moveType=absolute&panSpeed=3&tiltSpeed=4");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      mHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).moveTo(any(Position.class), eq(3), eq(4));
  }
  
  @Test
  public void testMalformedURI() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/move?pan=4&pan=3");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      mHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }
  
  @Test
  public void testMoveWithNotAllValues() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/move?pan=5");
    when(exchange.getRequestURI()).thenReturn(uri);
    mHandler.handle(exchange);
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }
  
  @Test
  public void testCameraConnectionException() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/focus?pan=1&tilt=2&moveType=absolute&panSpeed=3&tiltSpeed=4");
    when(exchange.getRequestURI()).thenReturn(uri);
    doThrow(new CameraConnectionException("test exception", 0)).when(cam).moveTo(any(Position.class), eq(3), eq(4));
    try {
      mHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }

}
