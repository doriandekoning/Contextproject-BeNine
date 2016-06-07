package com.benine.backend.http;

import com.benine.backend.LogEvent;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import javax.servlet.ServletException;

import static org.mockito.Mockito.*;

/**
 * Created on 22-05-16.
 */
public class LogHandlerTest extends RequestHandlerTest {

  @Override
  public RequestHandler supplyHandler() {
    return new LogHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, JSONException {
    super.initialize();
    when(requestMock.getRequestURI()).thenReturn("TESTPATH");
  }

  @Test
  public void testLog() throws IOException, ServletException {
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);


    verify(logger).log(matches(".*" + requestMock.getRequestURI()), eq(LogEvent.Type.INFO));
  }
}
