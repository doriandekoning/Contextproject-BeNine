package com.benine.backend.http;

import org.junit.Assert;
import org.junit.Test;

import java.util.jar.Attributes;


/**
 * Created by dorian on 4-5-16.
 */
public class RequestHandlerTest {

  @Test
  public final void testDecodeCorrectURI() throws MalformedURIException {
    Attributes expected = new Attributes();
    expected.putValue("id", "4");
    expected.putValue("Hello", "World!");
    Attributes actual = RequestHandler.parseURI("id=4&Hello=World!");
    Assert.assertEquals(expected, actual);
  }
  @Test(expected=MalformedURIException.class)
  public final void testDecodeMalformedURI() throws MalformedURIException {
    RequestHandler.parseURI("id=3&id=4");
  }
}
