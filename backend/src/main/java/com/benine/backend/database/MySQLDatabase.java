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
 * Created on 4-5-2016.
 */
public class MySQLDatabase implements Database {
  private Connection connection;
  private String user;
  private String password;
  private int presetID;


  /**
   * Constructor of a MySQL Database.
   *
   * @param user     username used to connect to the database.
   * @param password used to connect to the databse.
   */
  public MySQLDatabase(String user, String password) {
    connection = null;
    this.user = user;
    this.password = password;
    presetID = 1;
  }

  @Override
  public boolean isConnected() throws SQLException {
    return connection != null && !connection.isClosed();
  }

  @Override
  public void addPreset(Preset preset) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = createAddSqlQuery(preset);
      statement.executeUpdate(sql);
      preset.setId(presetID);
      presetID++;
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  @Override
  public void deletePreset(int presetID) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = "DELETE FROM presets WHERE ID = " + presetID;
      statement.executeUpdate(sql);
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  @Override
  public void updatePreset(Preset preset) throws SQLException {
    Statement statement = connection.createStatement();
    deletePreset(preset.getId());
    try {
      String sql = createAddSqlQuery(preset);
      statement.executeUpdate(sql);
      statement.close();
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  @Override
  public ArrayList<Preset> getAllPresets() throws SQLException {
    ArrayList<Preset> list = new ArrayList<Preset>();
    Statement statement = connection.createStatement();
    try {
      String sql = "SELECT id, pan, tilt, zoom, focus,"
          + " iris, autofocus, panspeed, tiltspeed, autoiris, image, camera_ID"
          + " FROM presetsDatabase.presets";
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
          + " autofocus, panspeed, tiltspeed, autoiris, image, camera_ID"
          + " FROM presetsDatabase.presets WHERE camera_ID = " + cameraId;
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
          new InputStreamReader(new FileInputStream("database" + File.separator
              + "databasefile.sql"), "UTF-8"));
      sr.runScript(reader);
      presetID = 1;
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
  public void addCamera(int id, String macAddress) {
    Statement statement = null;
    try {
      statement = connection.createStatement();
      final String sql = String.format("INSERT INTO presetsdatabase.camera VALUES(%s,'%s')",
          id, macAddress);
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
   *
   * @param resultset the list with all the presets
   * @return The preset from the resultset
   */
  public Preset getPresetsFromResultSet(ResultSet resultset) {
    try {
      Position pos = new Position(resultset.getInt("pan"), resultset.getInt("tilt"));
      int zoom = resultset.getInt("zoom");
      int focus = resultset.getInt("focus");
      int iris = resultset.getInt("iris");
      boolean autoFocus = resultset.getInt("autofocus") == 1;
      int panspeed = resultset.getInt("panspeed");
      int tiltspeed = resultset.getInt("tiltspeed");
      boolean autoIris = resultset.getInt("autoiris") == 1;
      // String image = resultset.getString("image");
      int id = resultset.getInt("camera_ID");
      return new Preset(pos, zoom, focus, iris, autoFocus, panspeed, tiltspeed,
          autoIris, id);
    } catch (Exception e) {
      e.printStackTrace();
      getLogger().log("Presets couldn't be retrieved.", LogEvent.Type.CRITICAL);
      return null;
    }
  }

  /**
   * Creates a sql query to insert a preset in the database.
   *
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
    return "INSERT INTO presetsdatabase.presets VALUES(" + presetID + ","
        + preset.getPosition().getPan() + "," + preset.getPosition().getTilt()
        + "," + preset.getZoom() + "," + preset.getFocus()
        + "," + preset.getIris() + "," + auto + "," + preset.getPanspeed() + ","
        + preset.getTiltspeed() + "," + autoir + ",'" + preset.getImage() + "',"
        + preset.getCameraId() + ")";
  }
  

  /**
   * Get the logger of the single servercontroller.
   * @return logger object.
   */
  private Logger getLogger() {
    return ServerController.getInstance().getLogger();
  }

}
