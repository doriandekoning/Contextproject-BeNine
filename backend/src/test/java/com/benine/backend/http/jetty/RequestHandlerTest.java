package com.benine.backend.http.jetty;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.http.RequestHandler;
import org.eclipse.jetty.server.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;


/**
 * Created on 4-5-16.
 */
public abstract class RequestHandlerTest {

  private ServerController serverController;
  private Logger logger = mock(Logger.class);
  private RequestHandler handler;

  PrintWriter out;
  String target;
  Request requestMock;
  CameraController cameracontroller;
  HttpServletResponse httpresponseMock;
  HttpServletRequest httprequestMock;


  public abstract RequestHandler supplyHandler();

  @Before
  public void initialize() throws IOException {
    handler = supplyHandler();
    cameracontroller = mock(CameraController.class);

    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    serverController.setLogger(logger);
    serverController.setCameraController(cameracontroller);

    out = mock(PrintWriter.class);
    target = "target";

    requestMock = mock(Request.class);
    httpresponseMock = mock(HttpServletResponse.class);
    httprequestMock = mock(HttpServletRequest.class);

    when(httpresponseMock.getWriter()).thenReturn(out);
  }

  public RequestHandler getHandler() {
    return handler;
  }

  @Test
  public final void testRespondStatus() throws IOException {
    String response = "response";
    handler.respond(requestMock, httpresponseMock, response);
    verify(httpresponseMock).setStatus(200);
  }

  @Test
  public final void testRespondStatusError() throws IOException {
    String response = "response";
    when(httpresponseMock.getWriter()).thenThrow(new IOException());

    handler.respond(requestMock, httpresponseMock, response);
    verify(httpresponseMock).setStatus(500);
  }

  @Test
  public final void testRespond() throws IOException {
    String response = "response";
    handler.respond(requestMock, httpresponseMock, response);
    verify(out).write(response);
    verify(out).close();
  }

  @Test
  public void testResponseMessageTrueStatus() throws Exception {
    handler.respondSuccess(requestMock, httpresponseMock);
    verify(httpresponseMock).setStatus(200);
  }
  
  @Test
  public void testResponseMessageFalseStatus() throws Exception {
    handler.respondFailure(requestMock, httpresponseMock);
    verify(httpresponseMock).setStatus(200);
  }

  @Test
  public void testResponseMessageTrueMessage() throws Exception {
    String response = "{\"succes\":\"true\"}";
    handler.respondSuccess(requestMock, httpresponseMock);
    verify(out).write(response);
  }

  @Test
  public void testResponseMessageFalseMessage() throws Exception {
    String response = "{\"succes\":\"false\"}";
    handler.respondFailure(requestMock, httpresponseMock);
    verify(out).write(response);
  }
}
