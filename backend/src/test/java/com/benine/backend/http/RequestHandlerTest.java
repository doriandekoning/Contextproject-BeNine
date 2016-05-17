package com.benine.backend.http;

import com.benine.backend.Logger;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.jar.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * Created by dorian on 4-5-16.
 */
public class RequestHandlerTest {
  
  private PresetCreationHandler handler;
  private HttpExchange exchangeMock;
  private OutputStream out;
  private Logger logger;
  
  @Before
  public void initialize(){
    logger = mock(Logger.class);
    handler = new PresetCreationHandler(logger);
    exchangeMock = mock(HttpExchange.class);
    out = mock(OutputStream.class);
    when(exchangeMock.getResponseBody()).thenReturn(out);
  }

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
    RequestHandler handler = new testRequestHandler(logger);
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
  
  @Test
  public void testGetLogger(){
    assertEquals(logger, handler.getLogger());
  }
  
  
  @Test
  public void testGetCameraByID() throws URISyntaxException {
    when(exchangeMock.getRequestURI()).thenReturn(new URI("http://localhost/camera/1/zoom?position=4"));
    assertTrue(handler.getCameraId(exchangeMock) == 1);
  }


  // Test used to be able to instantiate RequestHandler
  private class testRequestHandler extends RequestHandler {

    public testRequestHandler(Logger logger) {
      super(logger);
    }
    public void handle(HttpExchange e) {
      // Do nothing
    }
  }

}
