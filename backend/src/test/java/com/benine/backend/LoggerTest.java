package com.benine.backend;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import java.io.PrintStream;

/**
 * Created by dorian on 2-5-16.
 */
public class LoggerTest {

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  private final LogWriter logwriter = mock(LogWriter.class);

  private int counter = 0;

  @Before
  public void setUp() {
    // Catch the console output in out
    System.setOut(new PrintStream(out));
    doNothing().when(logwriter).write(anyString(), anyString(), any());
    doNothing().when(logwriter).write(anyString(), any());
    doNothing().when(logwriter).write(anyList());
    doNothing().when(logwriter).write(any(LogEvent.class));
  }

  @Test
  public void testLoggerToConsole() {
    Logger logger = new Logger(logwriter);
    logger.log("this moment", "Hello", LogEvent.Type.CRITICAL);
    Assert.assertEquals("[CRITICAL|this moment]Hello\n", out.toString());
    out.reset();
  }

  @Test
  public void testLoggerToFile() {
    // Check if one of the write methods is called on the logwriter mock
    doAnswer(count).when(logwriter).write(any(), any(), any());
    doAnswer(count).when(logwriter).write(any(), any());
    doAnswer(count).when(logwriter).write(anyList());
    doAnswer(count).when(logwriter).write(any(LogEvent.class));
    Logger logger = new Logger(logwriter);

    logger.log("this moment", "Hello", LogEvent.Type.CRITICAL);
    Assert.assertEquals(1, counter);
    counter=0;
    out.reset();
  }


  @Test
  public void testLoggerNotToConsoleWhenDisabled() {
    Logger logger = new Logger(logwriter);
    logger.disableConsoleLogging();
    logger.log("this moment", "Hello", LogEvent.Type.CRITICAL);
    Assert.assertEquals("", out.toString());
    out.reset();
  }

  @Test
  public void testLoggerNoToFileWhenDisabled() {
    // Check if one of the write methods is called on the logwriter mock
    doAnswer(count).when(logwriter).write(any(), any(), any());
    doAnswer(count).when(logwriter).write(any(), any());
    doAnswer(count).when(logwriter).write(anyList());
    doAnswer(count).when(logwriter).write(any(LogEvent.class));
    Logger logger = new Logger(logwriter);
    logger.disableFileLogging();
    logger.log("this moment", "Hello", LogEvent.Type.CRITICAL);
    Assert.assertEquals(0, counter);
    counter=0;
  }


  @Test
  public void testLoggerToConsoleWhenReEnabled() {
    Logger logger = new Logger(logwriter);
    logger.disableConsoleLogging();
    logger.enableConsoleLogging();
    logger.log("this moment", "Hello", LogEvent.Type.CRITICAL);
    Assert.assertEquals("", out.toString());
    out.reset();
  }

  @Test
  public void testLoggerToFileWhenReEnabled() {
    // Check if one of the write methods is called on the logwriter mock
    doAnswer(count).when(logwriter).write(any(), any(), any());
    doAnswer(count).when(logwriter).write(any(), any());
    doAnswer(count).when(logwriter).write(anyList());
    doAnswer(count).when(logwriter).write(any(LogEvent.class));
    Logger logger = new Logger(logwriter);
    logger.disableFileLogging();
    logger.enableFileLogging();
    logger.log("this moment", "Hello", LogEvent.Type.CRITICAL);
    Assert.assertEquals(0, counter);
    counter=0;
    out.reset();
  }

  @Test
  public void testLoggerDisableConsoleLogging() {
    Logger logger = new Logger(logwriter);
    logger.disableConsoleLogging();
    Assert.assertFalse(logger.consoleLoggingEnabled());
  }

  @Test
  public void testLoggerDisableFileLogging() {
    Logger logger = new Logger(logwriter);
    logger.disableFileLogging();
    Assert.assertFalse(logger.fileLoggingEnabled());
  }

  @Test
  public void testLoggerReEnableConsoleLogging() {
    Logger logger = new Logger(logwriter);
    logger.disableConsoleLogging();
    logger.enableConsoleLogging();
    Assert.assertTrue(logger.consoleLoggingEnabled());
  }

  @Test
  public void testLoggerReEnableFileLogging() {
    Logger logger = new Logger(logwriter);
    logger.disableFileLogging();
    logger.enableFileLogging();
    Assert.assertTrue(logger.fileLoggingEnabled());
  }

  private Answer count = invocation -> {
    counter++;
    return null;
  };
}
