package com.benine.backend;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the logevent class.
 */
public class LogEventTest {

  @Test
  public void TestCreateObjectTypeInt() {
    LogEvent event1 = new LogEvent("11:11", "TestEvent", 3);
    LogEvent event2 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO);
    Assert.assertEquals(event1, event2);
  }
  @Test
  public void TestEqualsEqual() {
    LogEvent event1 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO);
    LogEvent event2 = new LogEvent("11:11", "TestEvent", LogEvent.Type.INFO);
    Assert.assertEquals(event1, event2);
  }
}
