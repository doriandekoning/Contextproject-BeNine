package com.benine.backend.http;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.OutputStream;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * File handler test.
 */
public class FileHandlerTest {
  
  HttpExchange exchange = mock(HttpExchange.class);
  OutputStream out = mock(OutputStream.class);
  ServerController serverController = mock(ServerController.class);
  CameraController camController;
  
  @Before
  public void setUp() {
   camController = new CameraController();
   when(serverController.getCameraController()).thenReturn(camController);
  }
  
  @Test
  public void handleNonExcistingFile() throws Exception {
    
    URI uri = new  URI("http://localhost/public/test.jpg");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    FileHandler handler = new FileHandler();
    handler.handle(exchange);
    verify(out).write("{\"succes\":\"false\"}".getBytes());
  }
  
  @Test
  public void handleExcistingImage() throws Exception {
    Headers header = mock(Headers.class);
    URI uri = new  URI("http://localhost/resources/public/testpreset1_1.jpg");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    when(exchange.getResponseHeaders()).thenReturn(header);
    FileHandler handler = new FileHandler();
    handler.handle(exchange);
    verify(exchange).sendResponseHeaders(200, 0);
    verify(header).set("Content-Type",  "image/jpeg");
  }
  
  @Test
  public void handleExcistingTextFile() throws Exception {
    Headers header = mock(Headers.class);
    URI uri = new  URI("http://localhost/resources/public/test.txt");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    when(exchange.getResponseHeaders()).thenReturn(header);
    FileHandler handler = new FileHandler();
    handler.handle(exchange);
    verify(exchange).sendResponseHeaders(200, 0);
    verify(header).set("Content-Type",  "text/html");
  }
  
  @Test
  public void defaultMimeTypeTest() {
    String res = FileHandler.getMime("asdf/daf");
    assertEquals(res, "application/octet-stream");
  }
  
  @Test
  public void nonExcistingMimeTypeTest() {
    String res = FileHandler.getMime("asdf/daf.blabla.blabla");
    assertEquals(res, "application/octet-stream");
  }
  
  @Test
  public void htmlMimeTypeTest() {
    String res = FileHandler.getMime("asdf/daf.text.html");
    assertEquals(res, "text/html");
  }
  
  @Test
  public void jpgMimeTypeTest() {
    String res = FileHandler.getMime("asdf/daf.jpg");
    assertEquals(res, "image/jpeg");
  }

}
