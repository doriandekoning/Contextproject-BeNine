package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.mockito.Mockito.*;

/**
 * Created by dorian on 10-5-16.
 */

public class HttpControllerTest {

  private Logger logger = mock(Logger.class);
  private CameraController cameraController = mock(CameraController.class);
  private HttpController controller;
  private HttpServer mockserver;

  @Before
  public void setUp() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 4500);
    controller = new HttpController(address, logger, cameraController);

    mockserver = mock(HttpServer.class);
    controller.setServer(mockserver);
  }

  @After
  public void tearDown() {
    controller.destroy();
  }


  @Test
  public void testSetupBasicHandlers() {
    controller.setupBasicHandlers();
    Mockito.verify(mockserver).createContext(eq("/camera/"), any(CameraInfoHandler.class));
  }

  @Test
  public void testDestroy() {
    HttpServer server = mock(HttpServer.class);
    controller.setServer(server);

    controller.destroy();

    Mockito.verify(server).stop(0);
  }

}
