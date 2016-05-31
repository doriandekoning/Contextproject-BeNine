package com.benine.backend.video;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Observable;

/**
 * Defines a Stream connection.
 */
public class Stream extends Observable {

  private URLConnection connection;
  private URL url;
  private boolean connected;
  private Logger logger;
  private InputStream in;
  private PipedOutputStream out;
  private Thread streamThread;

  private final int BUFFERSPACE = (int) Math.pow(2, 13);

  /**
   * Constructor for a new stream object.
   *
   * @param streamurl The url to get the stream from.
   * @throws IOException If an exception occurs while creating the stream,
   *                     rendering the stream useless.
   */
  public Stream(String streamurl) throws IOException {
    this.url = new URL(streamurl);
    this.connected = false;
    this.logger = ServerController.getInstance().getLogger();
    this.out = new PipedOutputStream();

    this.streamThread = new Thread() {
      public void run() {
        provideStream();
      }
    };

    streamThread.start();

  }

  public void provideStream() {
    while (true) {
      if (!connected) {
        openConnection();
      }

      try {
        processStream();
      } catch (IOException e) {
        connected = false;
      }
    }
  }

  public void processStream() throws IOException {
    byte[] bytes = new byte[BUFFERSPACE];
    int bytesRead;

    if ((bytesRead = in.read(bytes)) != -1) {
      out.write(bytes, 0, bytesRead);
    } else {
      throw new IOException();
    }
  }

  /**
   * Opens a connection to the stream.
   */
  public void openConnection() {
    try {
      URLConnection conn = url.openConnection();
      this.connection = conn;

      conn.setConnectTimeout(5000);
      conn.connect();

      this.connected = true;
      this.in = conn.getInputStream();

      logger.log("Successfully connected to stream " + url.toString(),
              LogEvent.Type.INFO);
    } catch (IOException e) {
      logger.log("Could not connect to stream " + url.toString()
              + ", attempting to reestablish.", LogEvent.Type.WARNING);
    }
  }

  /**
   * Returns the inputstream of this Stream.
   *
   * @return An inputstream which can be read.
   */
  public InputStream getInputStream() {
    try {
      return new PipedInputStream(out);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
