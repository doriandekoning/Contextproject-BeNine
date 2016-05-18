package com.benine.backend.video;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Stream {

  private URL streamURL;
  private URLConnection connection;
  private InputStream inputstream;

  /**
   * Constructor for a new stream object.
   * @param url The url to get the stream from.
   */
  public Stream(URL url) {
    this.streamURL = url;
    this.connection = openConnection();
    this.inputstream = fetchInputStream();
  }

  /**
   * Opens a connection to the stream.
   * @return  The connection.
   */
  private URLConnection openConnection() {
    try {
      return streamURL.openConnection();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Fetches the inputstream from the connection.
   * @return  An the inputstream of the feed.
   */
  private InputStream fetchInputStream() {
    try {
      return connection.getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns the inputstream.
   * @return  The inputstream.
   */
  public InputStream getInputStream() {
    return this.inputstream;
  }

}
