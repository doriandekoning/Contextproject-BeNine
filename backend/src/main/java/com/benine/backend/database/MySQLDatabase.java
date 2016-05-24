package com.benine.backend.database;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.Preset;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.ibatis.common.jdbc.ScriptRunner;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Class for communicating with the MySQL Database.
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
  public void setConnection(Connection connect) {
    connection = connect;
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
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  @Override
  public void updatePreset(Preset preset) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      deletePreset(preset.getId());
      String sql = createAddSqlQuery(preset);
      statement.executeUpdate(sql);
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
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      return !connection.isClosed();
    } catch (SQLException e) {
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
      getLogger().log("Database is not reset.", LogEvent.Type.CRITICAL);
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
  public void checkCameras() throws SQLException {
    ArrayList<Camera> cameras = ServerController.getInstance().getCameraController().getCameras();
    ArrayList<String> macs = new ArrayList<String>();
    ResultSet resultset = null;
    Statement statement = null;
    try {
      statement = connection.createStatement();
      String sql = "SELECT ID, MACAddress FROM camera";
      resultset = statement.executeQuery(sql);
      checkOldCameras(resultset, cameras, macs);
      checkNewCameras(cameras, macs);
    } catch (SQLException e) {
      e.printStackTrace();
      getLogger().log("Cameras could not be gotten from database.", LogEvent.Type.CRITICAL);
    } catch (CameraConnectionException e) {
      e.printStackTrace();
    } finally {
      if (statement != null) {
        statement.close();
        resultset.close();
      }
    }
  }

  /**
   * Cheks if there are cameras in the database to be deleted.
   * @param result The resultset from the query
   * @param cameras The cameras
   * @param macs The MACAddresses of the cameras in the database
   * @throws SQLException No right connection to the database
   * @throws CameraConnectionException Not able to connect to the camera
   */
  public void checkOldCameras(ResultSet result, ArrayList<Camera> cameras, ArrayList<String> macs)
      throws SQLException, CameraConnectionException {
    while (result.next()) {
      boolean contains = false;
      String mac = result.getString("MACAddress");
      macs.add(mac);
      for (Camera camera : cameras) {
        if (camera.getMacAddress().equals(mac)) {
          contains = true;
          break;
        }
      }
      if (!contains) {
        deleteCamera(result.getInt("ID"));
      }
    }
  }

  /**
   * Checks if there are new cameras to be added to the database.
   * @param cameras The cameras
   * @param macs The MACAddresses of the cameras in the database
   * @throws CameraConnectionException Not able to connect to the camera
   */
  public void checkNewCameras(ArrayList<Camera> cameras, ArrayList<String> macs)
      throws CameraConnectionException {
    boolean contains = false;
    for (Camera camera : cameras) {
      for (String mac : macs) {
        if (mac.equals(camera.getMacAddress())) {
          contains = true;
          break;
        }
      }
      if (!contains) {
        addCamera(camera.getId(), camera.getMacAddress());
      }
    }
  }

  @Override
  public void deleteCamera(int cameraID) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = "DELETE FROM presets WHERE camera_ID = " + cameraID;
      statement.executeUpdate(sql);
      sql = "DELETE FROM camera WHERE ID = " + cameraID;
      statement.executeUpdate(sql);
    } finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  @Override
  public void useDatabase() throws SQLException {
    Statement statement = connection.createStatement();
    try {
      String sql = "USE presetsdatabase";
      statement.executeUpdate(sql);
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