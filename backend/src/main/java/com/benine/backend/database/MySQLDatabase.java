package com.benine.backend.database;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Position;
import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Ege on 4-5-2016.
 */
public class MySQLDatabase implements Database {
  private Connection connection;
  private int presetId;
  private String user;
  private String password;

  /**
   * Constructor of a MySQL Database.
   * @param user username used to connect to the database.
   * @param password used to connect to the databse.
   */
  public MySQLDatabase(String user, String password) {
    connection = null;
    presetId = 0;
    this.user = user;
    this.password = password;
  }

  @Override
  public boolean isConnected() throws SQLException {
    return connection != null && !connection.isClosed();
  }

  @Override
  public void addPreset(int camera, int cameraPresetNumber, Preset preset) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = createAddSqlQuery(preset);
      statement.executeUpdate(sql);
      sql = "INSERT INTO presetsdatabase.camerapresets VALUES(" + cameraPresetNumber + ","
          + camera + "," + presetId + ")";
      statement.executeUpdate(sql);
      statement.close();
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
    Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 0, 0, false);
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
      getLogger().log("Connection with database failed.", LogEvent.Type.CRITICAL);
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
      getLogger().log("Database check failed.", LogEvent.Type.CRITICAL);
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
      getLogger().log("Database is not reseted.", LogEvent.Type.CRITICAL);
      e.printStackTrace();
    }
  }

  @Override
  public void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception e) {
        getLogger().log("Database connection couldn't be closed.", LogEvent.Type.CRITICAL);
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
      getLogger().log("Camera couldn't be added", LogEvent.Type.CRITICAL);
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
      Preset preset = new Preset(new Position(0, 0), 0, 0, 0, false, 0, 0, false);
      preset.setPosition(new Position(resultset.getInt("pan"), resultset.getInt("tilt")));
      preset.setZoom(resultset.getInt("zoom"));
      preset.setFocus(resultset.getInt("focus"));
      preset.setIris(resultset.getInt("iris"));
      preset.setAutofocus(resultset.getInt("autofocus") == 1);
      preset.setPanspeed(resultset.getInt("panspeed"));
      preset.setTiltspeed(resultset.getInt("tiltspeed"));
      preset.setAutoiris(resultset.getInt("autoiris") == 1);
      preset.setImage(resultset.getString("image"));
      preset.setId(resultset.getInt("id"));
      return preset;
    } catch (Exception e) {
      e.printStackTrace();
      getLogger().log("Presets couldn't be retrieved.", LogEvent.Type.CRITICAL);
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
  

  /**
   * Get the logger of the single servercontroller.
   * @return logger object.
   */
  private Logger getLogger() {
    return ServerController.getInstance().getLogger();
  }

}
