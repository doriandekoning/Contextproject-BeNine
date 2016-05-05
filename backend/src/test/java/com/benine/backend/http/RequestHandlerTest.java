package com.benine.backend.http;

import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;


import java.io.OutputStream;
import java.util.jar.Attributes;


/**
 * Created by dorian on 4-5-16.
 */
public class RequestHandlerTest {

  @Test
  public final void testDecodeCorrectURI() throws MalformedURIException {
    Attributes expected = new Attributes();
    expected.putValue("id", "4");
    expected.putValue("Hello", "World!");
    Attributes actual = new testRequestHandler(null).parseURI("id=4&Hello=World!");
    Assert.assertEquals(expected, actual);
  }
  @Test(expected=MalformedURIException.class)
  public final void testDecodeMalformedURI() throws MalformedURIException {
    new testRequestHandler(null).parseURI("id=3&id=4");
  }

  @Test
  public final void testRespond() throws Exception {
    HttpExchange mock = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    when(mock.getResponseBody()).thenReturn(out);
    RequestHandler handler = new testRequestHandler(null);
    String response = "response";
    handler.respond(mock, response);
    verify(mock).sendResponseHeaders(200, response.length());
    verify(out).write(any());
    verify(out).close();
  }



  // Test used to be able to instantiate RequestHandler
  private class testRequestHandler extends RequestHandler {

    public testRequestHandler(CameraController controller) {
      super(controller);
    }
    public void handle(HttpExchange e) {
      // Do nothing
    }
  }
}
