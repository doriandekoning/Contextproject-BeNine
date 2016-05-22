package com.benine.backend.http.jetty;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.http.RequestHandler;
import org.eclipse.jetty.server.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;


/**
 * Created on 4-5-16.
 */
public abstract class RequestHandlerTest {

  private HttpServletResponse responseMock;
  private PrintWriter out;
  private ServerController serverController;
  private Logger logger = mock(Logger.class);
  private RequestHandler handler;
  private Request requestMock;

  public abstract RequestHandler supplyHandler();

  @Before
  public void initialize() throws IOException {
    handler = supplyHandler();
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    serverController.setLogger(logger);

    responseMock = mock(HttpServletResponse.class);
    requestMock = mock(Request.class);

    out = mock(PrintWriter.class);
    when(responseMock.getWriter()).thenReturn(out);
  }

  public Request getRequestMock() {
    return requestMock;
  }

  public RequestHandler getHandler() {
    return handler;
  }

  public HttpServletResponse getResponseMock() {
    return responseMock;
  }

  @Test
  public final void testRespondStatus() throws IOException {
    String response = "response";
    handler.respond(requestMock, responseMock, response);
    verify(responseMock).setStatus(200);
  }

  @Test
  public final void testRespondStatusError() throws IOException {
    String response = "response";
    when(responseMock.getWriter()).thenThrow(new IOException());

    handler.respond(requestMock, responseMock, response);
    verify(responseMock).setStatus(500);
  }

  @Test
  public final void testRespond() throws IOException {
    String response = "response";
    handler.respond(requestMock, responseMock, response);
    verify(out).write(response);
    verify(out).close();
  }

  @Test
  public void testResponseMessageTrueStatus() throws Exception {
    handler.respondSuccess(requestMock, responseMock);
    verify(responseMock).setStatus(200);
  }
  
  @Test
  public void testResponseMessageFalseStatus() throws Exception {
    handler.respondFailure(requestMock, responseMock);
    verify(responseMock).setStatus(200);
  }

  @Test
  public void testResponseMessageTrueMessage() throws Exception {
    String response = "{\"succes\":\"true\"}";
    handler.respondSuccess(requestMock, responseMock);
    verify(out).write(response);
  }

  @Test
  public void testResponseMessageFalseMessage() throws Exception {
    String response = "{\"succes\":\"false\"}";
    handler.respondFailure(requestMock, responseMock);
    verify(out).write(response);
  }
}
