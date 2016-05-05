package com.benine.backend.http;

import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;

import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraZoomHandlerTest {

  @Test
  public void testHandleNormal() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    when(exchange.getRequestURI()).thenReturn(new URI("id=4&zoomType=absolute&zoom=2"));
    when(exchange.getResponseBody()).thenReturn(out);

  }
}
