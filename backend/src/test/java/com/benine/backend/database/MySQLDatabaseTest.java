package com.benine.backend.database;

import com.benine.backend.Logger;
import com.benine.backend.Preset;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Ege on 4-5-2016.
 */
public class MySQLDatabaseTest extends BasicJDBCTestCaseAdapter {

    private MySQLDatabase database;
    StatementResultSetHandler statementHandler;

    @Before
    public void prepareEmptyResultSet() {
        MockConnection connection =
            getJDBCMockObjectFactory().getMockConnection();
        statementHandler =
            connection.getStatementResultSetHandler();
        database = new MySQLDatabase("root", "root");
    }

    @Test
    public final void testConnection() throws SQLException {
        database.connectToDatabaseServer();
        assertTrue(database.isConnected());
        database.closeConnection();
        verifyConnectionClosed();
    }

    @Test
    public final void testResetDatabase() {
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.closeConnection();
        verifySQLStatementExecuted("CREATE SCHEMA IF NOT EXISTS `presetsDatabase`");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testAddPreset() throws SQLException {
        Preset preset = new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, false, 0);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(preset);
        database.closeConnection();
        verifySQLStatementExecuted("insert into presets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testDeletePreset() throws SQLException {
        Preset preset = new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, false, 0);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(preset);
        database.deletePreset(1);
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM presets WHERE ID = 1");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testUpdatePreset() throws SQLException {
        Preset preset = new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, false, 0);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(preset);
        database.updatePreset(preset);
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM presets WHERE ID = 1");
        verifySQLStatementExecuted("insert into presets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetAllPreset() throws SQLException {
        Preset preset = new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, false, 0);
        ;
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(preset);
        ArrayList<Preset> result = database.getAllPresets();
        database.closeConnection();
        verifySQLStatementExecuted("SELECT id, pan, tilt, zoom, focus, iris, autofocus");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testAddCamera() throws SQLException {
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addCamera(1, "ip");
        database.closeConnection();
        verifySQLStatementExecuted("INSERT INTO presetsdatabase.camera VALUES(1,'ip')");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetPresetsCamera() throws SQLException {
        Preset preset = new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, false, 0);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addCamera(1, "ip");
        database.addPreset(preset);
        database.getAllPresetsCamera(1);
        database.closeConnection();
        verifySQLStatementExecuted("SELECT id, pan, tilt, zoom, focus, iris, autofocus");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testCheckCameras() throws SQLException {
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addCamera(1, "ip");
        database.checkCameras();
        database.closeConnection();
        verifySQLStatementExecuted("SELECT ID, MACAddress FROM camera");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testDeleteCamera() throws SQLException {
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addCamera(1, "ip");
        database.deleteCamera(1);
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM presets WHERE camera_ID = 1");
        verifySQLStatementExecuted("DELETE FROM camera WHERE ID = 1");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testUseDatabase() throws SQLException {
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.useDatabase();
        database.closeConnection();
        verifySQLStatementExecuted("USE presetsdatabase");
        verifyCommitted();
        verifyAllResultSetsClosed();
    }

    @Test
    public final void testCheckDatabase() throws SQLException {
        database.connectToDatabaseServer();
        MockResultSet result = statementHandler.createResultSet();
        statementHandler.prepareGlobalResultSet(result);
        Assert.assertFalse(database.checkDatabase());
        database.closeConnection();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetPresetsFromResultSet() throws SQLException {
        MockResultSet result = statementHandler.createResultSet();
        statementHandler.prepareGlobalResultSet(result);
        database.connectToDatabaseServer();
        result.addColumn("pan", new Object[]{1});
        result.addColumn("tilt", new Object[]{1});
        result.addColumn("zoom", new Object[]{1});
        result.addColumn("focus", new Object[]{1});
        result.addColumn("iris", new Object[]{1});
        result.addColumn("autofocus", new Object[]{1});
        result.addColumn("panspeed", new Object[]{1});
        result.addColumn("tiltspeed", new Object[]{1});
        result.addColumn("autoiris", new Object[]{1});
        result.addColumn("camera_ID", new Object[]{1});
        result.next();
        Preset preset = database.getPresetsFromResultSet(result);
        assertEquals(preset, new Preset(new Position(1,1),1,1,1,true,1,1,true,1));
    }

    @Test
    public final void testGetFailedPresetsFromResultSet() {
        MockResultSet result = statementHandler.createResultSet();
        database.connectToDatabaseServer();
        assertNull(database.getPresetsFromResultSet(result));
    }

    @Test
    public final void testCheckNewCameras() throws CameraConnectionException {
        Camera camera = mock(Camera.class);
        when(camera.getMacAddress()).thenReturn("mas");
        database.connectToDatabaseServer();
        ArrayList<Camera> list = new ArrayList<Camera>();
        ArrayList<String> macs = new ArrayList<String>();
        list.add(camera);
        macs.add("mac");
        database.checkNewCameras(list, macs);
        verifySQLStatementExecuted("INSERT INTO");
    }

    @Test
    public final void testOldCameras() throws CameraConnectionException, SQLException {
        Camera camera = mock(Camera.class);
        when(camera.getMacAddress()).thenReturn("mas");
        database.connectToDatabaseServer();
        ArrayList<Camera> list = new ArrayList<Camera>();
        ArrayList<String> macs = new ArrayList<String>();
        list.add(camera);
        MockResultSet result = statementHandler.createResultSet();
        statementHandler.prepareGlobalResultSet(result);
        result.addColumn("MACAddress", new Object[]{"mac"});
        result.addColumn("ID", new Object[]{1});
        database.checkOldCameras(result, list, macs);
        verifySQLStatementExecuted("DELETE FROM");
    }
}