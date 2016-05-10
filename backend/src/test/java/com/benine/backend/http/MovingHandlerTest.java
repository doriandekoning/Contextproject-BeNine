package com.benine.backend.http;

import com.benine.backend.camera.*;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;

import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dorian on 4-5-16.
 */
public class MovingHandlerTest {

  @Test
  public void testMoveAbsolute() throws Exception {
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    MovingCamera cam = mock(MovingCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    URI uri = new  URI("http://localhost/zoom?id=0&pan=1&tilt=2&moveType=absolute&panSpeed=3&tiltSpeed=4");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    MovingHandler fHandler = new MovingHandler(camController, 0);
    try {
      fHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    verify(cam).moveTo(any(Position.class), eq(3), eq(4));
  }

}
