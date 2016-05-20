package com.benine.backend;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Test class for the logevent class.
 */
public class LogEventTest {


  @Test
  public void TestEqualsDifferentLogEvent() {
    LogEvent event1 = new LogEvent("11:11", "TestEvent", LogEvent.Type.DEBUG);
    LogEvent event2 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO);
    Assert.assertNotEquals(event1, event2);
  }
  @Test
  public void TestEqualsEqual() {
    LogEvent event1 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO);
    LogEvent event2 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO);
    Assert.assertEquals(event1, event2);
  }
  @Test
  public void TestEqualsDifferentTime() {
    LogEvent event1 = new LogEvent("23:20", "TestEvent", LogEvent.Type.INFO);
    LogEvent event2 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO);
    Assert.assertNotEquals(event1, event2);
  }
  @Test
  public void TestEqualsDifferentDesc() {
    LogEvent event1 = new LogEvent("11:11", "OtherTestEvent", LogEvent.Type.INFO);
    LogEvent event2 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO);
    Assert.assertNotEquals(event1, event2);
  }
  @Test
  public void TestEqualsDifferentException() {
    LogEvent event1 = new LogEvent("11:11", "OtherTestEvent", LogEvent.Type.INFO, new NullPointerException());
    LogEvent event2 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO, new IOException());
    Assert.assertNotEquals(event1, event2);
  }
  @Test
  public void TestEqualsOneExceptionNull() {
    LogEvent event1 = new LogEvent("11:11", "OtherTestEvent", LogEvent.Type.INFO);
    LogEvent event2 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO, new IOException());
    Assert.assertNotEquals(event1, event2);
    Assert.assertNotEquals(event2, event1);
  }
  @Test
  public void testToStringNoException() {
    LogEvent event1 = new LogEvent("11:11", "Other Test Event", LogEvent.Type.INFO);
    Assert.assertEquals("[INFO|11:11]Other Test Event", event1.toString());
  }
  @Test
  public void testToStringException() {
    LogEvent event1 = new LogEvent("11:11", "Other Test Event", LogEvent.Type.INFO, new IOException("Error occurred"));
    String pattern = "\\[INFO\\|11\\:11\\]Other Test Event,.*";
    String string = event1.toString().replace(System.getProperty("line.separator"), "");
    Assert.assertTrue(string.matches(pattern));
  }
  @Test
  public void testGetType() {
    LogEvent event = new LogEvent("", "", LogEvent.Type.DEBUG);
    Assert.assertEquals(LogEvent.Type.DEBUG, event.getType());
  }
}
