package com.benine.backend.database;

import com.benine.backend.Config;
import com.benine.backend.Main;
import com.benine.backend.Preset;
import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

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

  /**
   * Constructor of a MySQL Database.
   */
  public MySQLDatabase() {
    connection = null;
    presetId = 0;
  }

  @Override
  public boolean isConnected() throws SQLException {
    return connection != null && !connection.isClosed();
  }

  @Override
  public void addPreset(int camera, int cameraPresetNumber, Preset preset) {
    int auto = 0;
    if (preset.isAutofocus()) {
      auto = 1;
    }
    int autoir = 0;
    if (preset.getAutoiris()) {
      autoir = 1;
    }
    try {
      Statement statement = connection.createStatement();
      String sql = "INSERT INTO presetsdatabase.presets VALUES(" + presetId + "," + preset.getPan()
          + "," + preset.getTilt() + "," + preset.getZoom() + "," + preset.getFocus()
          + "," + preset.getIris() + "," + auto + "," + preset.getPanspeed() + "," + preset.getTiltspeed()
          + "," + autoir + ")";
      statement.executeUpdate(sql);
      sql = "INSERT INTO presetsdatabase.camerapresets VALUES(" + cameraPresetNumber + "," + camera
          + "," + presetId + ")";
      statement.executeUpdate(sql);
      statement.close();
      presetId++;
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void deletePreset(int camera, int cameraPresetNumber) {
    try {
      Statement statement = connection.createStatement();
      String sql = "DELETE FROM presetsdatabase.camerapresets WHERE Camera_ID = " + camera
          + " AND CameraPresetID = " + cameraPresetNumber;
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updatePreset(int camera, int cameraPresetNumber, Preset preset) {
    int auto = 0;
    if (preset.isAutofocus()) {
      auto = 1;
    }
    int autoir = 0;
    if (preset.getAutoiris()) {
      autoir = 1;
    }
    try {
      Statement statement = connection.createStatement();
      String sql = "INSERT INTO presetsdatabase.presets VALUES(" + presetId + "," + preset.getPan()
          + "," + preset.getTilt() + "," + preset.getZoom() + "," + preset.getFocus()
          + "," + preset.getIris() + "," + auto + "," + preset.getPanspeed() + "," + preset.getTiltspeed()
          + "," + autoir + ")";
      statement.executeUpdate(sql);
      sql = "UPDATE presetsdatabase.camerapresets SET Presets_ID = " + presetId
          + "WHERE Camera_ID = " + camera + "AND CameraPresetID = " + cameraPresetNumber;
      statement.executeUpdate(sql);
      presetId++;
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Preset getPreset(int camera, int cameraPresetNumber) {
    Preset preset = new Preset(0, 0, 0, 0, 0, false, 0, 0, false);
    try {
      Statement statement = connection.createStatement();
      String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus, panspeed, tiltspeed, autoiris"
          + " FROM presetsDatabase.presets "
          + "JOIN presetsDatabase.camerapresets ON presetsDatabase.camerapresets.Presets_ID = "
          + "presetsDatabase.presets.ID " + "WHERE presetsDatabase.camerapresets.Camera_ID = "
          + camera + " AND presetsDatabase.camerapresets.CameraPresetID = " + cameraPresetNumber;
      ResultSet resultset = statement.executeQuery(sql);
      if (resultset.next()) {
        preset.setPan(resultset.getInt("pan"));
        preset.setTilt(resultset.getInt("tilt"));
        preset.setZoom(resultset.getInt("zoom"));
        preset.setFocus(resultset.getInt("focus"));
        preset.setIris(resultset.getInt("iris"));
        preset.setAutofocus(resultset.getInt("autofocus") == 1);
        preset.setPanspeed(resultset.getInt("panspeed"));
        preset.setTiltSpeed(resultset.getInt("tiltspeed"));
        preset.setAutoiris(resultset.getInt("autoiris") == 1);
      }
      resultset.close();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return preset;
  }

  @Override
  public ArrayList<Preset> getAllPresets() {
    ArrayList<Preset> list = new ArrayList<Preset>();
    try {
      Statement statement = connection.createStatement();
      String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus, panspeed, tiltspeed, autoiris "
          + "FROM presetsDatabase.presets JOIN camerapresets ON camerapresets.Preset_ID = presets.ID";
      ResultSet resultset = statement.executeQuery(sql);
      while (resultset.next()) {
        getPresetsFromResultSet(list, resultset);
      }
      resultset.close();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  @Override
  public ArrayList<Preset> getAllPresetsCamera(int cameraId) {
    ArrayList<Preset> list = new ArrayList<Preset>();
    try {
      Statement statement = connection.createStatement();
      String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus, panspeed, tiltspeed, autoiris"
          + " FROM presetsDatabase.presets " + "JOIN camerapresets ON camerapresets.Preset_ID = presets.ID "
          + "WHERE camerapresets.Camera_ID = " + cameraId;
      ResultSet resultset = statement.executeQuery(sql);
      while (resultset.next()) {
        getPresetsFromResultSet(list, resultset);
      }
      resultset.close();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  @Override
  public boolean connectToDatabaseServer() {
    Config config = Main.getConfig();
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      String connect = "jdbc:mysql://localhost:3306?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
      connection = DriverManager.getConnection(connect, config.getValue("sqluser"),
          config.getValue("sqlpassword"));
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      return !connection.isClosed();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean checkDatabase() {
    try {
      ResultSet databaseNames = connection.getMetaData().getCatalogs();
      if (databaseNames == null) {
        return false;
      }
      while (databaseNames.next()) {
        String databaseName = databaseNames.getString(1);
        if (databaseName.equals("presetsDatabase")) {
          return true;
        }
      }
      databaseNames.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void resetDatabase() {
    try {
      ScriptRunner sr = new ScriptRunner(connection, false, false);
      Reader reader = new BufferedReader(
          new FileReader("database/databasefile.sql"));
      sr.runScript(reader);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void addCamera(int id, String ip) {
    try {
      Statement statement = connection.createStatement();
      String sql = "INSERT INTO presetsdatabase.camera VALUES(" + id + "," + ip + ")";
      statement.executeUpdate(sql);
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getPresetsFromResultSet(ArrayList<Preset> list, ResultSet resultset) {
    try {
      Preset preset = new Preset(0, 0, 0, 0, 0, false, 0, 0, false);
      preset.setPan(resultset.getInt("pan"));
      preset.setTilt(resultset.getInt("tilt"));
      preset.setZoom(resultset.getInt("zoom"));
      preset.setFocus(resultset.getInt("focus"));
      preset.setIris(resultset.getInt("iris"));
      preset.setAutofocus(resultset.getInt("autofocus") == 1);
      preset.setPanspeed(resultset.getInt("panspeed"));
      preset.setTiltSpeed(resultset.getInt("tiltspeed"));
      preset.setAutoiris(resultset.getInt("autoiris") == 1);
      list.add(preset);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
