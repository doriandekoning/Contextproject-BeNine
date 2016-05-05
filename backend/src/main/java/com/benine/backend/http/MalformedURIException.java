package com.benine.backend.http;

/**
 * Created by dorian on 4-5-16.
 */

public class MalformedURIException extends Exception {
  /**
   * Creaates a new MallformedURIException.
   * @param message the message of the exception.
   */
  public MalformedURIException(String message) {
    super(message);
  }
}