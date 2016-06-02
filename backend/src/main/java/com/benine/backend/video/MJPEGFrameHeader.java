package com.benine.backend.video;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 01-06-16.
 */
public class MJPEGFrameHeader {

  private int contentlength;
  private String boundary;
  private String contenttype;

  private static final String MJPEG_BOUNDARY = "--polycast";
  private static final String CONTENT_TYPE = "image/jpeg";

  /**
   * Creates a frameheader object from a bytestream header.
   * @param header Header in bytes.
   */
  public MJPEGFrameHeader(byte[] header) {
    String headerString = new String(header, StandardCharsets.UTF_8);

    this.boundary = MJPEG_BOUNDARY;
    this.contenttype = CONTENT_TYPE;
    this.contentlength = getContentLength(headerString);

  }

  /**
   * Looks for the Content-Length: tag in the header, and extracts the value.
   *
   * @param header A header string.
   * @return 0 if content-length not found, else content length.
   */
  private int getContentLength(String header) {
    Pattern contentLength = Pattern.compile("Content-Length: \\d+");
    Matcher matcher = contentLength.matcher(header);

    // On a match, remove all non-digits and parse it to an integer.
    if (matcher.find()) {
      return Integer.parseInt(matcher.group().replaceAll("[^0-9]", ""));
    } else {
      return -1;
    }
  }

  /**
   * Returns a string formatted mjpeg header.
   * @return a String representation of the header.
   */
  public String getString() {
    StringBuilder builder = new StringBuilder(128);

    builder.append(boundary);
    builder.append('\n');
    builder.append("Content-Type: " + contenttype);
    builder.append('\n');
    builder.append("Content-Length: " + contentlength);
    builder.append('\n');
    builder.append('\n');

    return builder.toString();
  }

  /**
   * Returns the content length of this header.
   * @return  Integer value representing content length.
   */
  public int getContentlength() {
    return this.contentlength;
  }

  /**
   * Returns the boundary of this header.
   * @return  A String representation of the boundary.
   */
  public String getBoundary() {
    return this.boundary;
  }

  public void setContentLength(int length) {
    this.contentlength = length;
  }
}
