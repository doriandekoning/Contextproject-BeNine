package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.camera.*;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpControllerTest {

  private Logger logger = mock(Logger.class);
  private CameraController cameraController = mock(CameraController.class);
  private HttpController controller;
  private HttpServer mockserver;

  @Before
  public void setUp() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 3000);
    controller = new HttpController(address, logger, cameraController);

    mockserver = mock(HttpServer.class);
    // Close the created server, replace it with a mock.
    controller.destroy();
    controller.setServer(mockserver);
  }


  @Test
  public void testSetupBasicHandlers() {
    controller.setupBasicHandlers();
    Mockito.verify(mockserver).createContext(eq("/camera/"), any(CameraInfoHandler.class));
  }

  @Test
  public void testCreateFocusHandler() {
    FocussingCamera cam = mock(FocussingCamera.class);
    controller.createHandlers(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/focus"), any());
  }

  @Test
  public void testCreateIrisHandler() {
    IrisCamera cam =  mock(IrisCamera.class);
    controller.createHandlers(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/iris"), any());
  }

  @Test
  public void testCreateMovingHandler() {
    MovingCamera cam =  mock(MovingCamera.class);
    controller.createHandlers(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/move"), any());
  }

  @Test
  public void testCreateZoomHandler() {
    ZoomingCamera cam = mock(ZoomingCamera.class);
    controller.createHandlers(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/zoom"), any());
  }

  @Test
  public void testCreatePresetHandler() {
    Camera cam = mock(Camera.class);
    controller.createHandlers(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/preset"), any());
  }

  @Test
  public void testCreateRecallPresetHandler() {
    Camera cam = mock(Camera.class);
    controller.createHandlers(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/recallpreset"), any());
  }

  @Test
  public void testCreateCreatePresetHandler() {
    Camera cam = mock(Camera.class);
    controller.createHandlers(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/createpreset"), any());
  }

  @Test
  public void testDestroy() {
    controller.destroy();
    Mockito.verify(mockserver).stop(0);
  }

}
