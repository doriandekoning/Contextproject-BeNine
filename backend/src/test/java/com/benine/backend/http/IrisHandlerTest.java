package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.*;
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
public class IrisHandlerTest {
  
  HttpExchange exchange = mock(HttpExchange.class);
  OutputStream out = mock(OutputStream.class);
  IrisCamera cam = mock(IrisCamera.class);
  CameraController camController = mock(CameraController.class);
  ServerController serverController = mock(ServerController.class);
  IrisHandler iHandler;
  
  @Before
  public void setUp() {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    
    when(cam.getId()).thenReturn(1);
    when(camController.getCameraById(1)).thenReturn(cam);
    camController.addCamera(cam);
    serverController.setCameraController(camController);
    iHandler = new IrisHandler(mock(Logger.class));

    when(exchange.getResponseBody()).thenReturn(out);
  }

  @Test
  public void testOnlyAutoIris() throws Exception { 
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/zoom?autoIrisOn=true");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      iHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setAutoIrisOn(true);
  }


  @Test
  public void testOnlyIrisPosIris() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/iris?position=3");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      iHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setIrisPosition(3);
  }


  @Test
  public void testAutoIrisOnAndIrisPosIris() throws Exception {
    URI uri = new  URI("http://localhost/camera/" +cam.getId()+ "/iris?position=3&autoIrisOn=true");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      iHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).setIrisPosition(3);
    verify(cam).setAutoIrisOn(true);
  }
  
  @Test
  public void testMalformedURI() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+ "/iris?position=3&position=true");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      iHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }

  
  @Test
  public void testCameraConnectionException() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/iris?position=3");
    when(exchange.getRequestURI()).thenReturn(uri);
    doThrow(new CameraConnectionException("test exception", 0)).when(cam).setIrisPosition(eq(3));
    try {
      iHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String response = "{\"succes\":\"false\"}"; 
    verify(out).write(response.getBytes());
  }

  @Test
  public void testMoveIrisRelative() throws Exception {
    URI uri = new  URI("http://localhost/camera/"+cam.getId()+"/iris?speed=5");
    when(exchange.getRequestURI()).thenReturn(uri);
    try {
      iHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).moveIris(5);
  }
}
