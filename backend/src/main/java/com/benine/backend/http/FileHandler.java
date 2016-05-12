package com.benine.backend.http;

import com.benine.backend.camera.CameraController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * Handles static files like the images of a preset.
 */
public class FileHandler extends RequestHandler {
  
  /**
   * Constructor of this request handler.
   * @param controller of the camera's.
   */
  public FileHandler(CameraController controller) {
    super(controller);

  }

  /**
   * Handle incoming requests.
   * Checks if the file requested exists and returns it.
   * @param exchange incoming request.
   * @throws IOException when file could not be send.
   */
  public void handle(HttpExchange exchange) throws IOException {
    URI uri = exchange.getRequestURI();
    String path = uri.getPath();
    File file = new File("." + path).getCanonicalFile();
    //check if the requested file exists
    if (!file.isFile()) {
      respond(exchange, "{\"succes\":\"false\"}");
    } else {
      String mime = "text/html";
      if (path.substring(path.length() - 4).equals(".jpg")) {
        mime = "image/jpeg";           
      }

      Headers header = exchange.getResponseHeaders();
      header.set("Content-Type", mime);
      exchange.sendResponseHeaders(200, 0);              

      OutputStream os = exchange.getResponseBody();
      FileInputStream fs = new FileInputStream(file);
      final byte[] buffer = new byte[0x10000];
      int count = 0;
      while ((count = fs.read(buffer)) >= 0) {
        os.write(buffer,0,count);
      }
      fs.close();
      os.close();
    }  
  }
}