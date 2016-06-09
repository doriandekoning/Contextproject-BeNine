package com.benine.backend.database;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.performance.PresetQueue;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;

import com.benine.backend.preset.SimplePreset;
import com.ibatis.common.jdbc.ScriptRunner;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Class for communicating with the MySQL Database.
 */
public class MySQLDatabase implements Database {

  private Connection connection;
  private String user;
  private String password;
  private Logger logger;

  /**
   * Constructor of a MySQL Database.
   *
   * @param user     username used to connect to the database.
   * @param password used to connect to the databse.
   * @param logger   to use for the database
   */
  public MySQLDatabase(String user, String password, Logger logger) {
    connection = null;
    this.user = user;
    this.password = password;
    this.logger = logger;
  }

  @Override
  public void setConnection(Connection connect) {
    connection = connect;
  }

  @Override
  public Set<String> getTagsFromPreset(Preset preset) {
    Set<String> list = new HashSet<>();
    PreparedStatement statement = null;
    ResultSet resultset = null;
    try {
      String sql = "SELECT tag_name FROM tagPreset WHERE preset_ID = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, preset.getId());
      resultset = statement.executeQuery();
      while (resultset.next()) {
        list.add(resultset.getString("tag_name"));
      }
    } catch (SQLException e) {
      logger.log("Tags could not be gotten.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, resultset);
    }
    return list;
  }

  @Override
  public void addTagToPreset(String tag, Preset preset) {
    PreparedStatement statement = null;
    try {
      String sql = "INSERT INTO tagPreset VALUES(?,'?')";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, preset.getId());
      statement.setString(2, tag);
      statement.executeUpdate();
    } catch (Exception e) {
      logger.log("Tag couldn't be added to preset.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void deleteTagFromPreset(String tag, Preset preset) {
    PreparedStatement statement = null;
    try {
      String sql = "DELETE FROM tagPreset WHERE tag_Name = '?' AND preset_ID = ?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, tag);
      statement.setInt(2, preset.getId());
      statement.executeUpdate();
    } catch (Exception e) {
      logger.log("Tag couldn't be deleted.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public ArrayList<Preset> getPresetsList(int queueID) {
    ArrayList<Preset> list = new ArrayList<Preset>();
    PreparedStatement statement = null;
    ResultSet resultset = null;
    try {
      String sql = "SELECT preset_ID FROM presetsList WHERE queue_ID = ? ORDER BY Sequence";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, queueID);
      resultset = statement.executeQuery();
      while (resultset.next()) {
        list.add(ServerController.getInstance().getPresetController()
            .getPresetById(resultset.getInt("ID")));
      }
    } catch (Exception e) {
      logger.log("Presets couldn't be gotten from list.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, resultset);
    }
    return list;
  }

  @Override
  public void addPresetsList(ArrayList<Preset> presets, int queueID) {
    PreparedStatement statement = null;
    try {
      int sequence = 0;
      for (Preset preset : presets) {
        sequence++;
        String sql = "INSERT INTO presetsList VALUES(?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, sequence);
        statement.setInt(2, queueID);
        statement.setInt(3, preset.getId());
        statement.executeUpdate();
      }
    } catch (Exception e) {
      logger.log("List could not be added.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void deletePresetsList(int queueID) {
    PreparedStatement statement = null;
    try {
      String sql = "DELETE FROM presetsList WHERE queue_ID = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, queueID);
      statement.executeUpdate();
    } catch (Exception e) {
      logger.log("List could not be deleted.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public ArrayList<PresetQueue> getQueues() {
    ArrayList<PresetQueue> list = new ArrayList<>();
    Statement statement = null;
    ResultSet resultset = null;
    try {
      statement = connection.createStatement();
      String sql = "SELECT ID, name FROM queue";
      resultset = statement.executeQuery(sql);
      while (resultset.next()) {
        int id = resultset.getInt("ID");
        String name = resultset.getString("Name");
        ArrayList<Preset> presets = getPresetsList(id);
        PresetQueue queue = new PresetQueue(id, name, presets);
        list.add(queue);
      }
    } catch (Exception e) {
      logger.log("Queues could not be gotten from database.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, resultset);
    }
    return list;
  }

  @Override
  public void addQueue(PresetQueue queue) {
    PreparedStatement statement = null;
    try {
      String sql = "INSERT INTO queue VALUES(?,'?')";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, queue.getID());
      statement.setString(2, queue.getName());
      statement.executeUpdate();
      addPresetsList(queue.getQueue(), queue.getID());
    } catch (Exception e) {
      logger.log("Queue could not be added.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void deleteQueue(int ID) {
    PreparedStatement statement = null;
    try {
      deletePresetsList(ID);
      String sql = "DELETE FROM queue WHERE ID = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, ID);
      statement.executeUpdate();
    } catch (Exception e) {
      logger.log("Queue could not be deleted.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public boolean isConnected() throws SQLException {
    return connection != null && !connection.isClosed();
  }

  @Override
  public void addPreset(Preset preset) {
    PreparedStatement statement = null;
    try {
      String sql = preset.createAddSqlQuery();
      statement = connection.prepareStatement(sql);
      statement.executeUpdate();
      sql = "INSERT INTO preset VALUES(?)";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, preset.getId());
      statement.executeUpdate();
      for (String tag : preset.getTags()) {
        addTagToPreset(tag, preset);
      }
    } catch (Exception e) {
      logger.log("Presets could not be added.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void deletePreset(Preset preset) {
    PreparedStatement statement = null;
    try {
      if (preset != null) {
        deleteTagsFromPreset(preset);
        String sql = preset.createDeleteSQL();
        statement = connection.prepareStatement(sql);
        statement.executeUpdate();
        sql = "DELETE FROM preset WHERE ID = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, preset.getId());
        statement.executeUpdate();
      }
    } catch (Exception e) {
      logger.log("Presets could not be deleted.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void updatePreset(Preset preset) {
    deletePreset(preset);
    addPreset(preset);
  }

  @Override
  public ArrayList<Preset> getAllPresets() {
    ArrayList<Preset> list = new ArrayList<Preset>();
    list.addAll(getAllPresetsSQL("SELECT id, pan, tilt, zoom, focus,"
        + " iris, autofocus, panspeed, tiltspeed, autoiris, image, camera_ID"
        + " FROM presetsDatabase.IPpreset"));
    list.addAll(getAllPresetsSQL("SELECT id, image, camera_ID"
        + " FROM presetsDatabase.simplepreset"));
    for (Preset preset : list) {
      preset.addTags(getTagsFromPreset(preset));
    }
    return list;
  }

  /**
   * Method to get all presets based on a SQL statement
   *
   * @param sql statement to retrieve the presets.
   * @return Presets from the database.
   */
  private ArrayList<Preset> getAllPresetsSQL(String sql) {
    ArrayList<Preset> list = new ArrayList<Preset>();
    PreparedStatement statement = null;
    ResultSet resultset = null;
    try {
      statement = connection.prepareStatement(sql);
      resultset = statement.executeQuery();
      while (resultset.next()) {
        if (sql.contains("simplepreset")) {
          list.add(getSimplePresetsFromResultSet(resultset));
        } else {
          list.add(getIPCameraPresetFromResultSet(resultset));
        }
      }
    } catch (Exception e) {
      logger.log("Presets could not be gotten.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, resultset);
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
      boolean connected = !connection.isClosed();
      return connected;
    } catch (SQLException | ClassNotFoundException e) {
      logger.log("Connection with database failed.", LogEvent.Type.CRITICAL);
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
    }
    return false;
  }

  @Override
  public void resetDatabase() {
    try {
      ScriptRunner sr = new ScriptRunner(connection, false, false);
      sr.setLogWriter(null);
      Reader reader = new BufferedReader(
          new InputStreamReader(new FileInputStream("database" + File.separator
              + "databasefile.sql"), "UTF-8"));
      sr.runScript(reader);
    } catch (SQLException | IOException e) {
      logger.log("Database is not reset.", LogEvent.Type.CRITICAL);
    }
  }

  @Override
  public void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception e) {
        logger.log("Database connection couldn't be closed.", LogEvent.Type.CRITICAL);
      }
    }
  }

  @Override
  public void addCamera(int id, String macAddress) {
    PreparedStatement statement = null;
    try {
      String sql = "INSERT INTO camera VALUES(?,'?')";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      statement.setString(2, macAddress);
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.log("Camera couldn't be added", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void checkCameras(ArrayList<Camera> cameras) {
    ArrayList<String> macs = new ArrayList<String>();
    ResultSet resultset = null;
    Statement statement = null;
    try {
      statement = connection.createStatement();
      String sql = "SELECT ID, MACaddress FROM camera";
      resultset = statement.executeQuery(sql);
      checkOldCameras(resultset, cameras, macs);
      checkNewCameras(cameras, macs);
    } catch (SQLException | CameraConnectionException e) {
      logger.log("Cameras could not be gotten from database.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, resultset);
    }
  }

  /**
   * Checks if there are cameras in the database to be deleted.
   *
   * @param result  The resultset from the query
   * @param cameras The cameras
   * @param macs    The MACAddresses of the cameras in the database
   * @throws SQLException              No right connection to the database
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
   *
   * @param cameras The cameras
   * @param macs    The MACAddresses of the cameras in the database
   * @throws CameraConnectionException Not able to connect to the camera
   */
  public void checkNewCameras(ArrayList<Camera> cameras, ArrayList<String> macs)
      throws CameraConnectionException {
    for (Camera camera : cameras) {
      boolean contains = false;
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
  public void deleteCamera(int cameraID) {
    PreparedStatement statement = null;
    try {
      String sql = "DELETE FROM preset WHERE camera_ID = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, cameraID);
      statement.executeUpdate();
      sql = "DELETE FROM IPpreset WHERE camera_ID = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, cameraID);
      statement.executeUpdate();
      sql = "DELETE FROM simplepreset WHERE camera_ID = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, cameraID);
      statement.executeUpdate();
      sql = "DELETE FROM camera WHERE ID = ?";
      statement = connection.prepareStatement(sql);
      statement.setInt(1, cameraID);
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.log("Cameras could not be deleted from database.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void useDatabase() {
    Statement statement = null;
    try {
      statement = connection.createStatement();
      String sql = "USE presetsdatabase";
      statement.executeUpdate(sql);
    } catch (Exception e) {
      logger.log("Database could not be found.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void addTag(String name) {
    PreparedStatement statement = null;
    try {
      String sql = "INSERT INTO tag VALUES('?')";
      statement = connection.prepareStatement(sql);
      statement.setString(1, name);
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.log("Tag couldn't be added.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public void deleteTag(String name) {
    PreparedStatement statement = null;
    try {
      String sql = "DELETE FROM tag WHERE name = '?'";
      statement = connection.prepareStatement(sql);
      statement.setString(1, name);
      statement.executeUpdate();
    } catch (SQLException e) {
      logger.log("Tag couldn't be deleted.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }

  @Override
  public Collection<String> getTags() {
    Collection<String> list = new ArrayList<String>();
    Statement statement = null;
    ResultSet resultset = null;
    try {
      statement = connection.createStatement();
      String sql = "SELECT name FROM tag";
      resultset = statement.executeQuery(sql);
      while (resultset.next()) {
        list.add(resultset.getString("name"));
      }
    } catch (SQLException e) {
      logger.log("Tag couldn't be gotten.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, resultset);
    }
    return list;
  }

  /**
   * Getter for the presets from the list of presets.
   *
   * @param resultset the list with all the presets
   * @return The preset from the resultset
   */
  public IPCameraPreset getIPCameraPresetFromResultSet(ResultSet resultset) {
    try {
      Position pos = new Position(resultset.getInt("pan"), resultset.getInt("tilt"));
      int zoom = resultset.getInt("zoom");
      int focus = resultset.getInt("focus");
      int iris = resultset.getInt("iris");
      boolean autoFocus = resultset.getInt("autofocus") == 1;
      int panspeed = resultset.getInt("panspeed");
      int tiltspeed = resultset.getInt("tiltspeed");
      boolean autoIris = resultset.getInt("autoiris") == 1;
      int cameraId = resultset.getInt("camera_ID");
      int id = resultset.getInt("ID");
      IPCameraPreset preset = new IPCameraPreset(pos, zoom, focus, iris, autoFocus,
          panspeed, tiltspeed, autoIris, cameraId);
      preset.setId(id);
      preset.setImage(resultset.getString("image"));
      return preset;
    } catch (Exception e) {
      logger.log("IPCamerapresets couldn't be retrieved.", LogEvent.Type.CRITICAL);
      return null;
    }
  }

  /**
   * Getter for the simple presets from the list of presets.
   *
   * @param resultset the list with all the presets
   * @return The preset from the resultset
   */
  public SimplePreset getSimplePresetsFromResultSet(ResultSet resultset) {
    try {
      String image = resultset.getString("image");
      int cameraId = resultset.getInt("camera_ID");
      SimplePreset preset = new SimplePreset(cameraId);
      int id = resultset.getInt("id");
      preset.setId(id);
      preset.setImage(image);
      return preset;
    } catch (Exception e) {
      logger.log("Simple preset couldn't be retrieved.", LogEvent.Type.CRITICAL);
      return null;
    }
  }

  /**
   * Closes the resultset and statement.
   *
   * @param statement the statement to be closed
   * @param resultset the resultset to be closed
   */
  private void close(Statement statement, ResultSet resultset) {
    try {
      if (statement != null) {
        statement.close();
      }
      if (resultset != null) {
        resultset.close();
      }
    } catch (SQLException e) {
      logger.log("Statement or resultset could not be closed", LogEvent.Type.WARNING);
    }
  }

  @Override
  public void deleteTagsFromPreset(Preset preset) {
    PreparedStatement statement = null;
    try {
      String query = "DELETE FROM tagPreset WHERE preset_ID = ?";
      statement = connection.prepareStatement(query);
      statement.setInt(1, preset.getId());
      statement.executeUpdate();
    } catch (Exception e) {
      logger.log("All tags couldn't be deleted.", LogEvent.Type.CRITICAL);
    } finally {
      close(statement, null);
    }
  }
}