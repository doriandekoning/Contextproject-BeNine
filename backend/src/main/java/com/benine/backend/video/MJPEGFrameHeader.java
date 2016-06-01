package com.benine.backend.video;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 01-06-16.
 */
public class MJPEGFrameHeader {

  private String headerString;
  private int contentlength;
  private String boundary;
  private String contenttype;

  /**
   * Creates a frameheader object from a bytestream header.
   * @param header Header in bytes.
   */
  public MJPEGFrameHeader(byte[] header) {
    this.headerString = new String(header, StandardCharsets.UTF_8);

    this.boundary = "--polycast";
    this.contenttype = "image/jpeg";
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

  public int getContentlength() {
    return this.contentlength;
  }

  public String getBoundary() {
    return this.boundary;

}
