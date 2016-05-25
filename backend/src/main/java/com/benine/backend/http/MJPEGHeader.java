package com.benine.backend.http;

/**
 * Created on 24-05-16.
 */
public enum MJPEGHeader {

  CONTENT_TYPE("multipart/x-mixed-replace;boundary="),

  CACHE_CONTROL("no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0"),

  CONNECTION("close"),

  PRAGMA("no-cache"),

  EXPIRES("Thu, 01 Dec 1994 16:00:00 GMT");

  private final String contents;

  /**
   * Creates an MJPEGHeader ENUM.
   * @param contents The contents of the header.
   */
  MJPEGHeader(String contents) {
    this.contents = contents;
  }

  /**
   * Returns the contents of the header.
   * @return The contents of the header.
   */
  public String getContents() {
    return this.contents;
  }

}
