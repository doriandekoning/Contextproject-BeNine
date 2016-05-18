package com.benine.backend.video;

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

  /**
   * Constructor for a new stream object.
   * @param streamurl The url to get the stream from.
   * @throws IOException If an exception occurs while creating the stream,
   * rendering the stream useless.
   */
  public Stream(String streamurl) throws IOException {
    URL url = new URL(streamurl);
    openConnection(url);

    this.inputstream = fetchInputStream();
  }

  /**
   * Opens a connection to the stream.
   * @param streamURL The url of the stream to open the connection to.
   * @throws IOException If an error occurs opening the connection.
   */
  private void openConnection(URL streamURL) throws IOException {
    URLConnection conn = streamURL.openConnection();
    conn.setConnectTimeout(1000);

    this.connection = conn;
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
}
