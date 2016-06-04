package com.benine.backend.http;


import com.benine.backend.Config;
import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.preset.PresetController;
import com.benine.backend.video.StreamController;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

/**
 * Class responsible for starting and mutating the HTTP Stream Server.
 */
public class HTTPServer {

  private Server server;

  private CameraController cameraController;
  
  private PresetController presetController;
  
  private StreamController streamController;
  
  private Logger logger;
  
  private Config config;

  /**
   * Creates a new Jetty server for handling requests.
   * @param port        The port to start the server on.
   * @param serverController to interact with the the serverController.
   * @throws Exception  If the server cannot be started, thus rendering the application useless.
   */
  public HTTPServer(int port, ServerController serverController) throws Exception {
    //The Jetty server object.
    this.server = new Server(port);
    this.cameraController = serverController.getCameraController();
    this.streamController = serverController.getStreamController();
    this.presetController = serverController.getPresetController();
    this.logger = serverController.getLogger();
    this.config = serverController.getConfig();
    
    setUpHandlers();

    getLogger().log("Successfully setup endpoints", LogEvent.Type.INFO);
    server.start();
    getLogger().log("Server running at: http://localhost:" + port , LogEvent.Type.INFO);
  }

  /**
   * Sets up the server Context Handlers.
   */
  private void setUpHandlers() {
    ContextHandler cameraContext = new ContextHandler("/camera");
    cameraContext.setHandler(new CameraInfoHandler(this));

    ContextHandler presetContext = new ContextHandler("/presets");
    presetContext.setHandler(new PresetsHandler(this));

    ContextHandler fileserverContext = new ContextHandler("/static");
    ResourceHandler fileHandler = new ResourceHandler();
    fileHandler.setResourceBase("static");
    fileserverContext.setHandler(fileHandler);

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] {cameraContext, presetContext, fileserverContext });

    Handler logHandler = new LogHandler(this);
    HandlerList handlerList = new HandlerList();
    handlerList.addHandler(logHandler);
    handlerList.addHandler(contexts);

    server.setHandler(handlerList);
  }
  
  /**
   * Shuts down the server.
   * @throws Exception  If the server cannot be stopped.
   */
  public void destroy() throws Exception {
    server.stop();
  }

  /**
   * Returns the ServerController logger.
   * @return  A Logger object.
   */
  protected Logger getLogger() {
    return logger;
  }

  /**
   * Returns the cameraController
   * @return  A cameracontroller object.
   */
  protected CameraController getCameraController() {
    return cameraController;
  }
  
  /**
   * Returns the streamController to get the stream
   * @return stream Controller.
   */
  protected StreamController getStreamController() {
    return streamController;
  }

  /**
   * Returns the presetController
   * @return presetController to change the presets
   */
  protected PresetController getPresetController() {
    return presetController;
  }

  public Config getConfig() {
    return config;
  }
}
