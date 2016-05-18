package com.benine.backend.video;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Stream {

  private URLConnection connection;
  private InputStream inputstream;

  /**
   * Constructor for a new stream object.
   * @param streamurl The url to get the stream from.
   */
  public Stream(String streamurl) {
    try {
      URL url = new URL(streamurl);
      openConnection(url);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    this.inputstream = fetchInputStream();
  }

  /**
   * Opens a connection to the stream.
   * @param streamURL The url of the stream to open the connection to.
   */
  private void openConnection(URL streamURL) {
    try {
      URLConnection conn = streamURL.openConnection();
      conn.setConnectTimeout(1000);

      this.connection = conn;
    } catch (IOException e) {
      e.printStackTrace();
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
   * Returns the inputstream of this Stream.
   * @return  An inputstream which can be read.
   */
  public InputStream getInputStream() {
    return this.inputstream;
  }
}
