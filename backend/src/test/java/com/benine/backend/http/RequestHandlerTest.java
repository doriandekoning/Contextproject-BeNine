package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.util.jar.Attributes;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * Created by dorian on 4-5-16.
 */
public class RequestHandlerTest {
  
  private CameraController camera;
  private PresetCreationHandler handler;
  private HttpExchange exchangeMock;
  private OutputStream out;
  private Logger logger;
  
  @Before
  public void initialize(){
    camera = mock(CameraController.class);
    logger = mock(Logger.class);
    handler = new PresetCreationHandler(camera, logger);
    exchangeMock = mock(HttpExchange.class);
    out = mock(OutputStream.class);
    when(exchangeMock.getResponseBody()).thenReturn(out);
  }

  @Test
  public final void testDecodeCorrectURI() throws MalformedURIException {
    Attributes expected = new Attributes();
    expected.putValue("id", "4");
    expected.putValue("Hello", "World!");
    Attributes actual = new testRequestHandler(null, null).parseURI("id=4&Hello=World!");
    Assert.assertEquals(expected, actual);
  }
  @Test(expected=MalformedURIException.class)
  public final void testDecodeMalformedURI() throws MalformedURIException {
    new testRequestHandler(null, null).parseURI("id=3&id=4");
  }

  @Test
  public final void testRespond() throws Exception {
    RequestHandler handler = new testRequestHandler(null, logger);
    String response = "response";
    handler.respond(exchangeMock, response);
    verify(exchangeMock).sendResponseHeaders(200, response.length());
    verify(out).write(any());
    verify(out).close();
  }

  @Test
  public void testResponseMessageTrue() throws Exception {
    String response = "{\"succes\":\"true\"}"; 
    handler.responseSuccess(exchangeMock);
    verify(exchangeMock).sendResponseHeaders(200, response.length());
  }
  
  @Test
  public void testResponseMessageFalse() throws Exception {
    String response = "{\"succes\":\"false\"}"; 
    handler.responseFailure(exchangeMock);
    verify(exchangeMock).sendResponseHeaders(200, response.length());
  }


  // Test used to be able to instantiate RequestHandler
  private class testRequestHandler extends RequestHandler {

    public testRequestHandler(CameraController controller, Logger logger) {
      super(controller, logger);
    }
    public void handle(HttpExchange e) {
      // Do nothing
    }
  }

}
