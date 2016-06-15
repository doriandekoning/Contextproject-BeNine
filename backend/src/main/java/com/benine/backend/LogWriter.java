package com.benine.backend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to write to the log file.
 */
public class LogWriter {

  private long maxLogSize = 1000000;

  private int minLogLevel = 4;

  private String logLocation;

  private int logSize = 0;

  private volatile PrintWriter writer;

  private volatile ArrayList<LogEvent> buffer = new ArrayList<LogEvent>();

  // Semaphore for flushing
  private volatile boolean flushing = false;

  /**
   * Creates a new LogWriter, should be deleted by calling the destoy method.
   * @param logLocation Writes to a file
   * @throws IOException Throws an IOException when the loglocation is available.
   */
  public LogWriter(String logLocation) throws IOException {
    this.logLocation = logLocation;
    // Create logs dir if it does not exist
    Path path = Paths.get("logs");
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
    writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(logLocation + ".log"), StandardCharsets.UTF_8));
  }


  /**
   * Creates a LogEvent (at the current system time) and writes it to a file.
   * @param description the description of the logevent
   * @param type the type of the logevent
   */
  public void write(String description, LogEvent.Type type) {
    write(new LogEvent(System.currentTimeMillis() + "", description, type));
  }
  
  /**
   * Creates a LogEvent (at a specified time) and writes it to a file.
   * @param time the time the event happened
   * @param description the description of the logevent
   * @param type the type of the logevent
   */
  public void write(String time, String description, LogEvent.Type type) {
    write(new LogEvent(time, description, type));
  }
  
  /**
   * Writes LogEvent to file.
   * @param event event to write.
   */
  public void write(LogEvent event) {
    try {
      if (event.getType().getValue() > minLogLevel) {
        return;
      }
      // If the log level is high write the buffer and write this event immidiately
      // because this might indicate a crash (soon).
      if (event.getType().getValue() < 3) {
        flush();
        hardWrite(event);
      } else {
        buffer.add(event);
        int maxLogBufferSize = 25;
        if (buffer.size() > maxLogBufferSize) {
          flush();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes a list of logEvents to the list.
   * @param eventList list of events.
   */
  public void write(List<LogEvent> eventList) {
    for (LogEvent e : eventList) {
      write(e);
    }
  }

  /**
   * Flushes the buffer of this logwriter.
   */
  public void flush() {
    if (flushing) {
      // Because someone else is already flushing we can simply exit.
      return;
    }
    flushing = true;
    while (!buffer.isEmpty()) {
      hardWrite(buffer.get(0));
      buffer.remove(0);
    }
    writer.flush();
    flushing = false;
  }

  /**
   * Sets the minimum log level.
   * @param newMinLevel minimal level to log.
   */
  public void setMinLogLevel(int newMinLevel) {
    this.minLogLevel = newMinLevel;
  }

  /**
   * Sets the max log size.
   * @param maxLogSize max level to log.
   */
  public synchronized void setMaxLogSize(int maxLogSize) {
    this.maxLogSize = maxLogSize;
  }

  /**
   * Closes this Writer.
   */
  public void close() {
    flush();
  }

  /**
   * Writes to filewriter.
   * @param event event to write.
   */
  private synchronized void hardWrite(LogEvent event) {
    writer.write(event.toString() + "\n");
    logSize++;
    // Every 100 log items check log file size
    if (logSize % 100 == 0) {
      File oldFile = new File(logLocation + ".log");
      double fileSize = oldFile.length();
      if (fileSize > maxLogSize) {
        System.out.println("Trying to create a backup");
        // Check if old logfile exits if so delete it
        try {
          Files.delete(Paths.get(logLocation + "-old.log"));
        } catch (Exception e) {
          System.out.println(e);
        }
        try {
          File backupFile = new File(logLocation + "-old.log");
          writer.close();
          if (!oldFile.renameTo(backupFile)) {
            throw new IOException();
          }
          
          logSize = 0;
          FileOutputStream fileStream = new FileOutputStream(new File(logLocation  + ".log"));
          writer = new PrintWriter(new OutputStreamWriter(fileStream, "UTF-8"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

}
