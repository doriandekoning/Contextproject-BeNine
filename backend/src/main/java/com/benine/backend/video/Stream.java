package com.benine.backend.video;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Defines a Stream connection.
 */
public class Stream {

  private URLConnection connection;
  private InputStream inputstream;
  private URL url;
  private boolean connected;
  private Logger logger;

  /**
   * Constructor for a new stream object.
   * @param streamurl The url to get the stream from.
   * @throws IOException If an exception occurs while creating the stream,
   *      rendering the stream useless.
   */
  public Stream(String streamurl) throws IOException {
    this.url = new URL(streamurl);
    this.connected = false;
    this.logger = ServerController.getInstance().getLogger();

    openConnection();

    this.inputstream = fetchInputStream();
  }

  /**
   * Opens a connection to the stream.
   */
  public void openConnection() {
    while(!connected) {
      try {
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);
        conn.connect();

        this.connection = conn;
        this.connected = true;

        logger.log("Succesfully connected to stream " + url.toString(),
                LogEvent.Type.INFO);
      } catch (IOException e) {
        logger.log("Could not connect to stream " + url.toString()
                + ", attempting to reestablish.", LogEvent.Type.WARNING);
      }
    }
  }

  /**
   * Fetches the inputstream from the connection.
   * @return  An the inputstream of the feed.
   * @throws IOException If the inputstream cannot be read from the connection.
   */
  private InputStream fetchInputStream() throws IOException {
    return connection.getInputStream();
  }

  /**
   * Returns the inputstream of this Stream.
   * @return  An inputstream which can be read.
   */
  public InputStream getInputStream() {
    return this.inputstream;
  }

  public boolean isConnected() {
    return this.connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }
}
