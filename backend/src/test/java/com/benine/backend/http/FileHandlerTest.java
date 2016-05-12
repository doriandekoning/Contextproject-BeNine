package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.OutputStream;
import java.net.URI;

import org.junit.Test;

import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * File handler test.
 * @author Bryan
 *
 */
public class FileHandlerTest {
  
  @Test
  public void handleNonExcistingFile() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    CameraController camController = new CameraController();
    URI uri = new  URI("http://localhost/public/test.jpg");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    FileHandler handler = new FileHandler(camController);
    handler.handle(exchange);
    verify(out).write("{\"succes\":\"false\"}".getBytes());
  }
  
  @Test
  public void handleExcistingImage() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    Headers header = mock(Headers.class);
    CameraController camController = new CameraController();
    URI uri = new  URI("http://localhost/resources/public/testpreset1_1.jpg");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    when(exchange.getResponseHeaders()).thenReturn(header);
    FileHandler handler = new FileHandler(camController);
    handler.handle(exchange);
    verify(exchange).sendResponseHeaders(200, 0);
    verify(header).set("Content-Type",  "image/jpeg");
  }
  
  @Test
  public void handleExcistingTextFile() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    Headers header = mock(Headers.class);
    CameraController camController = new CameraController();
    URI uri = new  URI("http://localhost/resources/public/test.txt");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    when(exchange.getResponseHeaders()).thenReturn(header);
    FileHandler handler = new FileHandler(camController);
    handler.handle(exchange);
    verify(exchange).sendResponseHeaders(200, 0);
    verify(header).set("Content-Type",  "text/html");
  }
  
  

}
