package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.StreamDistributer;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;

public class CameraStreamHandler extends RequestHandler {

  public CameraStreamHandler(Logger logger) {
    super(logger);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    getLogger().log("Got an http request with uri: " + exchange.getRequestURI(), LogEvent.Type.INFO);

    int camID = getCameraId(exchange);

    StreamReader streamReader = null;
    try {
      streamReader = ServerController.getInstance().getStreamController().getStreamReader(camID);
    } catch (StreamNotAvailableException e) {
      getLogger().log(e.toString(), LogEvent.Type.WARNING);
    }

    if (streamReader instanceof MJPEGStreamReader) {
      MJPEGStreamReader streamReaderMJPEG = (MJPEGStreamReader) streamReader;
      StreamDistributer distributer = new StreamDistributer(streamReaderMJPEG);

      Headers header = exchange.getResponseHeaders();
      header.set("Cache-Control", "no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0");
      header.set("Connection", "close");
      header.set("Pragma", "no-cache");
      header.set("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
      header.set("Content-Type", "multipart/x-mixed-replace;boundary=" + streamReaderMJPEG.getBoundary());
      exchange.sendResponseHeaders(200, 0);

      PipedInputStream in = new PipedInputStream(distributer.getStream());
      BufferedInputStream bs = new BufferedInputStream(in);
      OutputStream os = exchange.getResponseBody();

      while (bs.available() > -1) {
        os.write(bs.read());
      }

      bs.close();

      os.flush();
      os.close();
    } else {
      responseFailure(exchange);
    }
  }
}
