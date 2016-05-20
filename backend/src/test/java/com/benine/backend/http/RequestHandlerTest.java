package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
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
  private ServerController serverController;
  private Logger logger = mock(Logger.class);
  
  @Before
  public void initialize(){
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    serverController.setLogger(logger);
    handler = new PresetCreationHandler();
    exchangeMock = mock(HttpExchange.class);
    out = mock(OutputStream.class);
    when(exchangeMock.getResponseBody()).thenReturn(out);
  }

  @Test
  public final void testDecodeCorrectURI() throws MalformedURIException {
    Attributes expected = new Attributes();
    expected.putValue("id", "4");
    expected.putValue("Hello", "World!");
    Attributes actual = new testRequestHandler().parseURI("id=4&Hello=World!");
    Assert.assertEquals(expected, actual);
  }
  
  @Test(expected=MalformedURIException.class)
  public final void testDecodeMalformedURI() throws MalformedURIException {
    new testRequestHandler().parseURI("id=3&id=4");
  }

  @Test
  public final void testRespond() throws Exception {
    RequestHandler handler = new testRequestHandler();
    String response = "response";
    handler.respond(exchangeMock, response);
    verify(exchangeMock).sendResponseHeaders(200, response.length());
    verify(out).write(any());
    verify(out).close();
  }

  @Test
  public void testResponseMessageTrue() throws Exception {
    String response = "{\"succes\":\"true\"}"; 
    handler.respondSuccess(exchangeMock);
    verify(exchangeMock).sendResponseHeaders(200, response.length());
  }
  
  @Test
  public void testResponseMessageFalse() throws Exception {
    String response = "{\"succes\":\"false\"}"; 
    handler.respondFailure(exchangeMock);
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

    public void handle(HttpExchange e) {
      // Do nothing
    }
  }

}
