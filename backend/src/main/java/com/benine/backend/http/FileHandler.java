package com.benine.backend.http;

import com.benine.backend.camera.CameraController;
import com.google.common.collect.ImmutableMap;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

/**
 * Handles static files like the images of a preset.
 */
public class FileHandler extends RequestHandler {
  
  /**
   * Map of extensions to MIME types.
   */
  static final Map<String, String> MIME_MAP = ImmutableMap.of(
      "txt", "text/html",
      "jpg", "image/jpeg",
      "html", "text/html"
  );
  
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
      
      String mime = getMime(path);
     
      Headers header = exchange.getResponseHeaders();
      header.set("Content-Type", mime);
      exchange.sendResponseHeaders(200, 0);              

      OutputStream os = exchange.getResponseBody();
      BufferedInputStream bs = new BufferedInputStream(new FileInputStream(file));
      while (bs.available() > 0) {
        os.write(bs.read());
      }
      bs.close();
      os.close();
    }  
  }

  /**
   * Method to get the mime type of a path to a file.
   * @param path to a file.
   * @return mime type of the file.
   */
  static final String getMime(String path) {
    String[] split = path.split("\\.(?=[^\\.]+$)");

    if (split.length == 2 && MIME_MAP.containsKey(split[1])) {
      return MIME_MAP.get(split[1]);           
    }
    
    return "application/octet-stream";
  }
}