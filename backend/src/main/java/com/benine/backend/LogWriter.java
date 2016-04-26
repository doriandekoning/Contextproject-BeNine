package com.benine.backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to write to the log file.
 */
public class LogWriter {

  //TODO get this from config
  private int maxLogBufferSize = 25;

  private long maxLogSize = 1000000;

  private int minLogLevel = 4;

  private String logLocation;

  private int logSize = 0;

  private PrintWriter writer;

  private ArrayList<LogEvent> buffer = new ArrayList<LogEvent>();

  /**
   * Creates a new LogWriter, should be deleted by calling the destoy method.
   * @param logLocation Writes to a file
   * @throws IOException Throws an IOException when the loglocation is available.
   */
  public LogWriter(String logLocation) throws IOException {
    this.logLocation = logLocation;
    writer = new PrintWriter(new FileWriter(logLocation + ".log"));
  }
  /**
   * Writes LogEvent to file.
   */
  public void write(LogEvent event) throws IOException {
    if(event.getType().getValue()>minLogLevel) {
      return;
    }
    // If the log level is high write the buffer and write this event immidiately
    // because this might indicate a crash (soon).
    if (event.getType().getValue() < 3) {
      flush();
      hardWrite(event);
    }else {
      buffer.add(event);
      if (buffer.size() > maxLogBufferSize) {
        flush();
      }
    }
  }
  /**
   * Writes a list of logEvents to the list.
   */
  public void write(List<LogEvent> eventList) throws IOException {
    for(LogEvent e : eventList) {
      write(e);
    }
  }
  /**
   * Flushes the buffer of this logwriter.
   */
  public void flush() {
    while (!buffer.isEmpty()) {
      hardWrite(buffer.get(0));
      buffer.remove(0);
    }
    writer.flush();
  }
  /**
   * Sets the minimum log level.
   */
  public void setMinLogLevel(int newMinLevel) {
    this.minLogLevel = newMinLevel;
  }
  /**
   * Sets the max log size.
   */
  public void setMaxLogSize(int maxLogSize) {
    this.maxLogSize = maxLogSize;
  }
  /**
   * Closes this Writer.
   */
  public void close() {
    flush();
  }
  /**
   * Writes to filewriter
   */
  private void hardWrite(LogEvent event) {
    writer.write(event.toString()+ "\n");
    logSize++;
    // Every 100 log items check log file size
    if(logSize%100 == 0) {
      File oldFile = new File(logLocation + ".log");
      double fileSize = oldFile.length();
      if(fileSize>maxLogSize) {
        System.out.println("Trying to create a backup");
        // Check if old logfile exits if so delete it
        try{
          Files.delete(Paths.get(logLocation + "-old.log"));
        } catch (Exception e) {

        }
        try{
          File backupFile = new File(logLocation + "-old.log");
          oldFile.renameTo(backupFile);
          logSize = 0;
          writer = new PrintWriter(new FileWriter(logLocation  + ".log"));
        } catch (Exception e) {

        }
      }
    }
  }

}
