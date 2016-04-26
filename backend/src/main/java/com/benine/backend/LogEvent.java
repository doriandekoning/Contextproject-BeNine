package com.benine.backend;

/**
 * Represents an event that happens and has to be logged.
 */
public class LogEvent {
  private String time, description;
  private Exception exception;
  private Type type;

  public enum Type {

    /**
     * Critical, program crashes or gets in an unrecoverable state.
     */
    CRITICAL(1),
    /**
     * Warns about an event that could possibly cause malfunction of the software.
     */
    WARNING(2),
    /**
     * Info about general events that happen in the system.
     */
    INFO(3),
    /**
     * Debug, used for debugging, more detailled info about events in the system.
     */
    DEBUG(4),
    /**
     * Trace logs everything that happens in the system and could possibly be of any interest when debugging.
     */
    TRACE(5);

    private final int level;

    Type(int level) {
      this.level = level;
    }

    public int getValue() {
      return level;
    }

  }

  /**
   * Creates a new logEvent
   * @param time time the event happened
   * @param description description of the event
   * @param type int indicating the log level
   */
  public LogEvent(String time, String description, Type type) {
    this.time = time;
    this.description = description;
    this.type = type;
  }
}
