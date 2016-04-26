package com.benine.backend;

import org.junit.Assert;
import org.junit.Test;

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
}
