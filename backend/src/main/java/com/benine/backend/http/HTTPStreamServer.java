package com.benine.backend.http;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;

public class HTTPStreamServer {

  private int port;
  private Server server;

  public HTTPStreamServer (int port) throws Exception {
    this.port = port;
    this.server = new Server(port);

    // Mutable handler collection
    HandlerCollection contextHandlerCollection = new HandlerCollection(true);

    ContextHandler cameraContext = new ContextHandler("/camera");
    cameraContext.setHandler(new CameraHandler());

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] { cameraContext });

    server.setHandler(contexts);

    server.start();
    server.join();
  }


}
