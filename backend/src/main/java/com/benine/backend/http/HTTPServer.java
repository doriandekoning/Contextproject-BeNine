package com.benine.backend.http;


import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
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

  /**
   * Creates a new Jetty server for handling requests.
   * @param port        The port to start the server on.
   * @throws Exception  If the server cannot be started, thus rendering the application useless.
   */
  public HTTPServer(int port) throws Exception {
    /*
    The Jetty server object.
   */
    this.server = new Server(port);

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
    cameraContext.setHandler(new CameraInfoHandler());

    ContextHandler presetContext = new ContextHandler("/presets");
    presetContext.setHandler(new PresetsHandler());

    ContextHandler fileserverContext = new ContextHandler("/static");
    ResourceHandler fileHandler = new ResourceHandler();
    fileHandler.setResourceBase("static");
    fileserverContext.setHandler(fileHandler);

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] {cameraContext, presetContext, fileserverContext });

    Handler logHandler = new LogHandler();
    HandlerList handlerList = new HandlerList();
    handlerList.addHandler(logHandler);
    handlerList.addHandler(contexts);

    server.setHandler(handlerList);
  }

  /**
   * Returns the ServerController logger.
   * @return  A Logger object.
   */
  private Logger getLogger() {
    return ServerController.getInstance().getLogger();
  }

  /**
   * Shuts down the server.
   * @throws Exception  If the server cannot be stopped.
   */
  public void destroy() throws Exception {
    server.stop();
  }
}
