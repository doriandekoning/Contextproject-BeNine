package com.benine.backend.http;

/**
 * Created on 4-5-16.
 */

public class MalformedURIException extends Exception {
  
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new MallformedURIException.
   * @param message the message of the exception.
   */
  public MalformedURIException(String message) {
    super(message);
  }
}