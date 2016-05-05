package com.benine.backend.database;

import com.benine.backend.Config;
import com.benine.backend.Main;
import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Ege on 4-5-2016.
 */
public class MySQLDatabase implements Database{
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
    public void addPreset(int camera, int cameraPresetNumber, DatabasePreset preset) {
        int auto = 0;
        if(preset.isAutofocus()){
            auto = 1;
        }
        try {Statement statement = connection.createStatement();
            String sql = "INSERT INTO presetsdatabase.presets VALUES(" + presetId + "," + preset.getPan() + "," + preset.getTilt() +
                    "," + preset.getZoom() + "," + preset.getFocus() + "," + preset.getIris() +"," + auto + ")";
            statement.executeUpdate(sql);
            sql = "INSERT INTO presetsdatabase.camerapresets VALUES(" + cameraPresetNumber + "," + camera + "," + presetId + ")";
            statement.executeUpdate(sql);
            statement.close();
            presetId++;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deletePreset(int camera, int cameraPresetNumber) {
        try {Statement statement = connection.createStatement();
            String sql = "DELETE FROM presetsdatabase.camerapresets WHERE Camera_ID = " + camera + " AND CameraPresetID = " + cameraPresetNumber;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePreset(int camera, int cameraPresetNumber, DatabasePreset preset) {
        int auto = 0;
        if(preset.isAutofocus()){
            auto = 1;
        }
        try {Statement statement = connection.createStatement();
            String sql = "INSERT INTO presetsdatabase.presets VALUES(" + presetId + "," + preset.getPan() + "," + preset.getTilt() +
                    "," + preset.getZoom() + "," + preset.getFocus() + "," + preset.getIris() +"," + auto + ")";
            statement.executeUpdate(sql);
            sql = "UPDATE presetsdatabase.camerapresets SET Presets_ID = " + presetId + "WHERE Camera_ID = " + camera +
                    "AND CameraPresetID = " + cameraPresetNumber;
            statement.executeUpdate(sql);
            presetId++;
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DatabasePreset getPreset(int camera, int cameraPresetNumber) {
        DatabasePreset preset = new DatabasePreset(0, 0, 0, 0, 0, false);
        try {Statement statement = connection.createStatement();
            String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets JOIN " +
                    "presetsDatabase.camerapresets ON presetsDatabase.camerapresets.Presets_ID = presetsDatabase.presets.ID " +
                    "WHERE presetsDatabase.camerapresets.Camera_ID = " + camera + " AND presetsDatabase.camerapresets.CameraPresetID = "+ cameraPresetNumber;
            ResultSet resultset = statement.executeQuery(sql);
            if(resultset.next()){
                preset.setPan(resultset.getInt("pan"));
                preset.setTilt(resultset.getInt("tilt"));
                preset.setZoom(resultset.getInt("zoom"));
                preset.setFocus(resultset.getInt("focus"));
                preset.setIris(resultset.getInt("iris"));
                preset.setAutofocus(resultset.getInt("autofocus") == 1);
            }
            resultset.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preset;
    }

    @Override
    public ArrayList<DatabasePreset> getAllPresets() {
        ArrayList<DatabasePreset> list = new ArrayList<DatabasePreset>();
        try {Statement statement = connection.createStatement();
            String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets JOIN camerapresets ON camerapresets.Preset_ID = presets.ID";
            ResultSet resultset = statement.executeQuery(sql);
            while(resultset.next()){
                DatabasePreset preset = new DatabasePreset(0, 0, 0, 0, 0, false);
                preset.setPan(resultset.getInt("pan"));
                preset.setTilt(resultset.getInt("tilt"));
                preset.setZoom(resultset.getInt("zoom"));
                preset.setFocus(resultset.getInt("focus"));
                preset.setIris(resultset.getInt("iris"));
                preset.setAutofocus(resultset.getInt("autofocus") == 1);
                list.add(preset);
            }
            resultset.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<DatabasePreset> getAllPresetsCamera(int cameraID) {
        ArrayList<DatabasePreset> list = new ArrayList<DatabasePreset>();
        try {Statement statement = connection.createStatement();
            String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets JOIN camerapresets ON camerapresets.Preset_ID = presets.ID" +
                    "WHERE camerapresets.Camera_ID = " + cameraID;
            ResultSet resultset = statement.executeQuery(sql);
            while(resultset.next()){
                DatabasePreset preset = new DatabasePreset(0, 0, 0, 0, 0, false);
                preset.setPan(resultset.getInt("pan"));
                preset.setTilt(resultset.getInt("tilt"));
                preset.setZoom(resultset.getInt("zoom"));
                preset.setFocus(resultset.getInt("focus"));
                preset.setIris(resultset.getInt("iris"));
                preset.setAutofocus(resultset.getInt("autofocus") == 1);
                list.add(preset);
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
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    config.getValue("sqluser"), config.getValue("sqlpassword"));
        }
        catch (SQLException | ClassNotFoundException e) {
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
        try{
            ResultSet databaseNames = connection.getMetaData().getCatalogs();
            if(databaseNames == null){
                return false;
            }
            while (databaseNames.next()) {
                String databaseName = databaseNames.getString(1);
                if(databaseName.equals("presetsDatabase")) return true;
            }
            databaseNames.close();
        }
        catch(Exception e){
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
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addCamera(int id, int ip, String name) {
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO presetsdatabase.camera VALUES(" + id + "," + name + "," + ip + ")";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
