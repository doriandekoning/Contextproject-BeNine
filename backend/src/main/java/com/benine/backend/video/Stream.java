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
  private PipedInputStream pipedInputStream;
  private Thread streamThread;

  private final int BUFFER = 8192;

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
    this.pipedInputStream = new PipedInputStream(out);

    this.streamThread = new Thread() {
      public void run() {
        process();
      }
    };

    streamThread.start();

  }

  /**
   * Processes the stream, and maintains the connection.
   */
  public void process() {
    while (true) {
      if (!connected) {
        openConnection();
      } else {
        try {
          streamToOutputstream();
        } catch (IOException e) {
          connected = false;
        }
      }
    }
  }

  /**
   * Reads the stream and adds it to the outputstream of this instance.
   * This prevents the stream from ending if the connection to the
   * stream ends, because it does not send the '-1' termination symbol to
   * the stream.
   * @throws IOException If the stream cannot be read.
   */
  public void streamToOutputstream() throws IOException {
    byte[] bytes = new byte[BUFFER];
    int bytesRead;

    if ((bytesRead = in.read(bytes)) > 0) {
      out.write(bytes, 0, bytesRead);
    } else {
      throw new IOException();
    }
  }

  /**
   * Opens a connection to the stream and reconnects on failure.
   */
  private void openConnection() {
    try {
      URLConnection conn = url.openConnection();
      this.connection = conn;

      conn.connect();

      this.connected = true;
      this.in = conn.getInputStream();

      logger.log("Connected to stream " + url.toString(), LogEvent.Type.INFO);
    } catch (IOException e) {
      logger.log("Could not connect to stream " + url.toString()
              + ", attempting to reestablish.", LogEvent.Type.WARNING);

      // Only retry once every 5 seconds.
      wait(5000);
    }
  }

  private void wait(int duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      logger.log("Stream thread interrupted while sleeping", LogEvent.Type.INFO);
    }
  }

  /**
   * Returns a readable inputstream of this Stream.
   * @return A PipedInputStream which can be read.
   * @throws IOException if the stream cannot be sent to the outputstream.
   */
  public InputStream getInputStream() throws IOException {
      return pipedInputStream;
  }
}
