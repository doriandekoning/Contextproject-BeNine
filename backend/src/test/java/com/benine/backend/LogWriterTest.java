package com.benine.backend;

import org.junit.Test;

import java.io.IOException;

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
}
