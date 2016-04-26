package com.benine.backend;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by dorian on 26-4-16.
 */
public class LogWriterTest {
  @Test
  public void testCreateLogWriter() throws Exception {
    // Check this doesnt throw an exception
    LogWriter logWriter = new LogWriter("logs/log1.log");
    logWriter.close();
  }
  @Test(expected=IOException.class)
  public void testCreateLogWriterException() throws Exception {
    // This should throw an exception
    LogWriter logWriter = new LogWriter("nonexsistentdirectory/log1.log");
    logWriter.close();
  }
  @Test
  public void testWriteLogHighType() throws Exception {
    LogWriter logWriter = new LogWriter("logs/testlog.log");
    LogEvent event = new LogEvent("42:42:42", "This is a testEvent", LogEvent.Type.CRITICAL);
    logWriter.write(event);
    List<String> contents = Files.readAllLines(Paths.get("logs/testlog.log"));
    Assert.assertEquals(contents.get(0), event.toString());
    logWriter.close();
  }
  @Test
  public void testWriteLogLowType() throws Exception {
    LogWriter logWriter = new LogWriter("logs/testlog.log");
    LogEvent event = new LogEvent("42:42:42", "This is a testEvent", LogEvent.Type.TRACE);
    logWriter.write(event);
    logWriter.flush();
    List<String> contents = Files.readAllLines(Paths.get("logs/testlog.log"));
    Assert.assertEquals(contents.get(0), event.toString());
    logWriter.close();
  }
}
