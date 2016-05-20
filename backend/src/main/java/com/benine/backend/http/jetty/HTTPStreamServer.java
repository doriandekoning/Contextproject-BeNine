package com.benine.backend.http.jetty;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;

/**
 * Class responsible for starting and mutating the HTTP Stream Server.
 */
public class HTTPStreamServer {

  /**
   * Creates a new Jetty server for handling requests.
   * @param port        The port to start the server on.
   * @throws Exception  If the server cannot be started, thus rendering the application useless.
   */
  public HTTPStreamServer(int port) throws Exception {
    /*
    The Jetty server object.
   */
    Server server = new Server(port);

    Handler logHandler = new LogHandler();

    ContextHandler cameraContext = new ContextHandler("/camera");
    cameraContext.setHandler(new CameraHandler());

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] { logHandler, cameraContext });

    HandlerList handlerList = new HandlerList();
    handlerList.addHandler(logHandler);
    handlerList.addHandler(contexts);

    server.setHandler(handlerList);

    server.start();
    server.join();
  }



}
