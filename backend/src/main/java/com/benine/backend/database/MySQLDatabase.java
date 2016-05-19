package com.benine.backend.database;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.Preset;
import com.benine.backend.camera.Position;
import com.ibatis.common.jdbc.ScriptRunner;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Ege on 4-5-2016.
 */
public class MySQLDatabase implements Database {
  private Connection connection;
  private int presetId;
  private String user;
  private String password;
  private Logger logger;

  /**
   * Constructor of a MySQL Database.
   * @param user username used to connect to the database.
   * @param password used to connect to the databse.
   * @param logger used to log important info. 
   */
  public MySQLDatabase(String user, String password, Logger logger) {
    connection = null;
    presetId = 0;
    this.user = user;
    this.password = password;
    this.logger = logger;
  }

  @Override
  public boolean isConnected() throws SQLException {
    return connection != null && !connection.isClosed();
  }

  @Override
  public void addPreset(int camera, Preset preset) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = createAddSqlQuery(preset);
      statement.executeUpdate(sql);
      sql = "INSERT INTO presetsdatabase.camerapresets VALUES(" + 0 + ","
          + camera + "," + presetId + ")";
      statement.executeUpdate(sql);
      statement.close();
      preset.setId(presetId);
      presetId++;
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  @Override
  public void deletePreset(int camera, int cameraPresetNumber) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = "DELETE FROM presetsdatabase.camerapresets WHERE Camera_ID = " + camera
          + " AND CameraPresetID = " + cameraPresetNumber;
      statement.executeUpdate(sql);
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  @Override
  public void updatePreset(int camera, int cameraPresetNumber, Preset preset) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = createAddSqlQuery(preset);
      statement.executeUpdate(sql);
      sql = "UPDATE presetsdatabase.camerapresets SET Preset_ID = " + presetId
          + "WHERE Camera_ID = " + camera + "AND CameraPresetID = " + cameraPresetNumber;
      statement.executeUpdate(sql);
      presetId++;
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  @Override
  public Preset getPreset(int camera, int cameraPresetNumber) throws SQLException {
    Preset preset = null;
    Statement statement = connection.createStatement();
    try {
      String sql = "SELECT id, pan, tilt, zoom, focus, iris, autofocus, panspeed,"
          + " tiltspeed, autoiris, image"
          + " FROM presetsDatabase.presets "
          + "JOIN presetsDatabase.camerapresets ON presetsDatabase.camerapresets.Preset_ID = "
          + "presetsDatabase.presets.ID " + "WHERE presetsDatabase.camerapresets.Camera_ID = "
          + camera + " AND presetsDatabase.camerapresets.CameraPresetID = " + cameraPresetNumber;
      ResultSet resultset = statement.executeQuery(sql);
      if (resultset.next()) {
        preset = getPresetsFromResultSet(resultset);
      }
      resultset.close();
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
    return preset;
  }

  @Override
  public ArrayList<Preset> getAllPresets() throws SQLException {
    ArrayList<Preset> list = new ArrayList<Preset>();
    Statement statement = connection.createStatement();
    try {
      String sql = "SELECT id, pan, tilt, zoom, focus,"
          + " iris, autofocus, panspeed, tiltspeed, autoiris, image"
          + " FROM presetsDatabase.presets JOIN camerapresets ON camerapresets.Preset_ID "
          + "= presets.ID";
      ResultSet resultset = statement.executeQuery(sql);
      while (resultset.next()) {
        list.add(getPresetsFromResultSet(resultset));
      }
      resultset.close();
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
    return list;
  }

  @Override
  public ArrayList<Preset> getAllPresetsCamera(int cameraId) throws SQLException {
    ArrayList<Preset> list = new ArrayList<Preset>();
    Statement statement = connection.createStatement();
    try {
      String sql = "SELECT id, pan, tilt, zoom, focus, iris,"
          + " autofocus, panspeed, tiltspeed, autoiris, image"
          + " FROM presetsDatabase.presets " + "JOIN camerapresets ON camerapresets.Preset_ID = "
          + "presets.ID WHERE camerapresets.Camera_ID = " + cameraId;
      ResultSet resultset = statement.executeQuery(sql);
      while (resultset.next()) {
        list.add(getPresetsFromResultSet(resultset));
      }
      resultset.close();
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
    return list;
  }

  @Override
  public boolean connectToDatabaseServer() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      String connect = "jdbc:mysql://localhost:3306?useUnicode=true&useJDBCCompliantTimezoneShift="
          + "true&useLegacyDatetimeCode=false&serverTimezone=UTC";
      connection = DriverManager.getConnection(connect, user, password);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      return !connection.isClosed();
    } catch (Exception e) {
      logger.log("Connection with database failed.", LogEvent.Type.CRITICAL);
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean checkDatabase() {
    try {
      ResultSet databaseNames = connection.getMetaData().getCatalogs();
      while (databaseNames.next()) {
        String databaseName = databaseNames.getString(1);
        if (databaseName.equals("presetsdatabase")) {
          return true;
        }
      }
      databaseNames.close();
    } catch (Exception e) {
      logger.log("Database check failed.", LogEvent.Type.CRITICAL);
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void resetDatabase() {
    try {
      ScriptRunner sr = new ScriptRunner(connection, false, false);
      Writer w = new OutputStreamWriter(new FileOutputStream("logs"
          + File.separator + "database-presetsdatabase.log"), "UTF-8");
      sr.setLogWriter(new PrintWriter(w));
      Reader reader = new BufferedReader(
          new InputStreamReader( new FileInputStream("database" + File.separator
              + "databasefile.sql"), "UTF-8"));
      sr.runScript(reader);
      presetId = 0;
    } catch (Exception e) {
      logger.log("Database is not reseted.", LogEvent.Type.CRITICAL);
      e.printStackTrace();
    }
  }

  @Override
  public void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception e) {
        logger.log("Database connection couldn't be closed.", LogEvent.Type.CRITICAL);
        e.printStackTrace();
      }
    }
  }

  @Override
  public void addCamera(int id, String ip) {
    Statement statement = null;
    try {
      statement = connection.createStatement();
      final String sql = String.format("INSERT INTO presetsdatabase.camera VALUES(%s,'%s')",
                                                                                  id, ip);
      statement.executeUpdate(sql);
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
      logger.log("Camera couldn't be added", LogEvent.Type.CRITICAL);
    } finally {
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public void useDatabase() throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = "USE presetsdatabase";
      statement.executeUpdate(sql);
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  /**
   * Getter for the presets from the list of presets.
   * @param resultset the list with all the presets
   * @return The preset from the resultset
   */
  public Preset getPresetsFromResultSet(ResultSet resultset) {
    try {
      Position pos  = new Position(resultset.getInt("pan"), resultset.getInt("tilt"));
      int zoom = resultset.getInt("zoom");
      int focus = resultset.getInt("focus");
      int iris = resultset.getInt("iris");
      boolean autoFocus = resultset.getInt("autofocus") == 1;
      int panspeed = resultset.getInt("panspeed");
      int tiltspeed = resultset.getInt("tiltspeed");
      boolean autoIris = resultset.getInt("autoiris") == 1;
      String image = resultset.getString("image");
      int id = resultset.getInt("id");
      return new Preset(pos, zoom, focus, iris, autoFocus, panspeed, tiltspeed,
                          autoIris, -1);
    } catch (Exception e) {
      e.printStackTrace();
      logger.log("Presets couldn't be retrieved.", LogEvent.Type.CRITICAL);
      return null;
    }
  }

  /**
   * Creates a sql query to insert a preset in the database.
   * @param preset The preset to insert
   * @return The query
   */
  public String createAddSqlQuery(Preset preset) {
    int auto = 0;
    if (preset.isAutofocus()) {
      auto = 1;
    }
    int autoir = 0;
    if (preset.isAutoiris()) {
      autoir = 1;
    }
    return "INSERT INTO presetsdatabase.presets VALUES(" + presetId + "," 
        + preset.getPosition().getPan() + "," + preset.getPosition().getTilt() 
        + "," + preset.getZoom() + "," + preset.getFocus()
        + "," + preset.getIris() + "," + auto + "," + preset.getPanspeed() + ","
        + preset.getTiltspeed() + "," + autoir + ",'" + preset.getImage() + "')";
  }
}
