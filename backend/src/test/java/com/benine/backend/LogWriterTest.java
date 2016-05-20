package com.benine.backend;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 26-4-16.
 */
public class LogWriterTest {
  @Before
  public void init() throws IOException {
    // Create logs dir if it does not exist
    Path p = Paths.get("logs");
    if (!Files.exists(p)) {
      Files.createDirectories(p);
    }
  }

  @Test
  public void testCreateLogWriter() throws Exception {
    // Check this doesnt throw an exception
    LogWriter logWriter = new LogWriter("logs/log1");
    logWriter.close();
  }
  @Test(expected=IOException.class)
  public void testCreateLogWriterException() throws Exception {
    // This should throw an exception
    LogWriter logWriter = new LogWriter("nonexsistentdirectory/log1");
    logWriter.close();
  }
  @Test
  public void testWriteLogHighType() throws Exception {
    LogWriter logWriter = new LogWriter("logs/testlog");
    LogEvent event = new LogEvent("42:42:42", "This is a testEvent", LogEvent.Type.CRITICAL);
    logWriter.write(event);
    logWriter.close();
    List<String> contents = Files.readAllLines(Paths.get("logs/testlog.log"));
    Assert.assertEquals(contents.get(0), event.toString());
  }
  @Test
  public void testWriteMoEvent() throws Exception {
    LogWriter logWriter = new LogWriter("logs/testlog");
    logWriter.write("10:10", "desc", LogEvent.Type.CRITICAL);
    LogEvent refEvent = new LogEvent("10:10", "desc", LogEvent.Type.CRITICAL);
    logWriter.close();
    List<String> contents = Files.readAllLines(Paths.get("logs/testlog.log"));
    Assert.assertEquals(contents.get(0), refEvent.toString());
  }
  @Test
  public void testWriteLogLowType() throws Exception {
    LogWriter logWriter = new LogWriter("logs/testlog");
    LogEvent event = new LogEvent("42:42:42", "This is a testEvent", LogEvent.Type.DEBUG);
    logWriter.write(event);
    logWriter.flush();
    List<String> contents = Files.readAllLines(Paths.get("logs/testlog.log"));
    Assert.assertEquals(contents.get(0), event.toString());
    logWriter.close();
  }
  @Test
  public void testWriteLogMinLogLevel() throws Exception {
    LogWriter logWriter = new LogWriter("logs/testlog");
    logWriter.setMinLogLevel(1);
    new LogEvent("42:42:42", "This is a testEvent", LogEvent.Type.DEBUG);
  }
  
  @Test
  public void testWriteLogItemList() throws Exception {
    LogWriter logWriter = new LogWriter("logs/multipletestlog");
    LogEvent event = new LogEvent("12:12:12", "This is a testEvent", LogEvent.Type.DEBUG);
    LogEvent otherevent = new LogEvent("24:24:24", "This is a another event", LogEvent.Type.CRITICAL);
    List<LogEvent> list = new ArrayList<LogEvent>();
    list.add(event);
    list.add(otherevent);
    List<String> stringList = new ArrayList<String>();
    stringList.add(event.toString());
    stringList.add(otherevent.toString());
    logWriter.write(list);
    logWriter.close();
    List<String> contents = Files.readAllLines(Paths.get("logs/multipletestlog.log"));
    Assert.assertEquals(stringList, contents);
  }
  
  @Test
  public void testMaxLogSize() throws Exception {
    LogWriter writer = new LogWriter("logs/maxlogsizetestlog");
    LogEvent event = new LogEvent("event", "description", LogEvent.Type.DEBUG);
    writer.setMaxLogSize(370);
    for(int i = 0; i < 110; i ++) {
      writer.write(event);
    }
    writer.close();
    List<String> contents = Files.readAllLines(Paths.get("logs/maxlogsizetestlog.log"));
    Assert.assertEquals(contents.get(0), event.toString());
    Assert.assertEquals(10, contents.size());
    contents = Files.readAllLines(Paths.get("logs/maxlogsizetestlog-old.log"));
    Assert.assertEquals(contents.get(0), event.toString());
  }
}
