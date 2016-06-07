package com.benine.backend.database;


import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;
import com.benine.backend.preset.SimplePreset;
import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created on 4-5-2016.
 */
public class MySQLDatabaseTest extends BasicJDBCTestCaseAdapter {

    private MySQLDatabase database;
    StatementResultSetHandler statementHandler;
    Logger logger = mock(Logger.class);

    @Before
    public void prepareEmptyResultSet() {
        MockConnection connection =
            getJDBCMockObjectFactory().getMockConnection();
        statementHandler =
            connection.getStatementResultSetHandler();
        database = new MySQLDatabase("root", "root", logger);
        database.connectToDatabaseServer();
        database.setConnection(connection);
    }

    @Test
    public final void testConnection() throws SQLException {
        assertTrue(database.isConnected());
        database.closeConnection();
        verifyConnectionClosed();
    }

    @Test
    public final void testResetDatabase() {
        database.resetDatabase();
        database.closeConnection();
        verifySQLStatementExecuted("CREATE SCHEMA IF NOT EXISTS `presetsDatabase`");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testAddPreset() throws SQLException {
        Preset preset = getPreset();
        database.resetDatabase();
        database.addPreset(preset);
        database.closeConnection();
        verifySQLStatementExecuted("insert into presets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testAddNullPreset() throws SQLException {
        database.resetDatabase();
        database.addPreset(null);
        database.closeConnection();
        verifySQLStatementNotExecuted("insert into presets");
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testDeletePreset() throws SQLException {
        Preset preset = getPreset();

        database.resetDatabase();
        database.addPreset(preset);
        database.deletePreset(preset);
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM presets WHERE ID = -1");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testUpdatePreset() throws SQLException {
        Preset preset = getPreset();
        preset.setId(1);

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
    public final void testUpdateNullPreset() throws SQLException {
        database.resetDatabase();
        database.updatePreset(null);
        database.closeConnection();
        verifySQLStatementNotExecuted("insert into presets");
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetAllPreset() throws SQLException {
        IPCameraPreset preset = getPreset();
        preset.setPanspeed(1);
        database.resetDatabase();
        database.addPreset(preset);
        database.closeConnection();
        verifySQLStatementExecuted("INSERT INTO presetsdatabase.presets VALUES(-1,1.0,1.0,1,1,1,1,1,1,0,'null',0)");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testAddCamera() throws SQLException {
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
        Preset preset = getPreset();
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
        database.resetDatabase();
        database.addCamera(1, "ip");
        database.checkCameras(new ArrayList<>());
        database.closeConnection();
        verifySQLStatementExecuted("SELECT ID, MACAddress FROM camera");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testDeleteCamera() throws SQLException {
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
        database.resetDatabase();
        database.useDatabase();
        database.closeConnection();
        verifySQLStatementExecuted("USE presetsdatabase");
        verifyCommitted();
        verifyAllResultSetsClosed();
    }

    @Test
    public final void testCheckDatabase() throws SQLException {
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
        result.addColumn("id", new Object[]{1});
        result.addColumn("image", new Object[]{"test"});
        result.next();
        IPCameraPreset preset = database.getIPCameraPresetFromResultSet(result);
        IPCameraPreset expected = getPreset();
        expected.setImage("test");
        expected.setId(1);
        assertEquals(expected, preset);
    }
    
    @Test
    public final void testGetSimplePresetsFromResultSet() throws SQLException {
        MockResultSet result = statementHandler.createResultSet();
        statementHandler.prepareGlobalResultSet(result);
        result.addColumn("camera_ID", new Object[]{1});
        result.addColumn("id", new Object[]{1});
        result.addColumn("image", new Object[]{"test"});
        result.next();
        SimplePreset preset = database.getSimplePresetsFromResultSet(result);
        SimplePreset expected =new SimplePreset(1);
        expected.setImage("test");
        expected.setId(1);
        result.addColumn("image", new Object[]{1});
        result.next();
        expected.setImage("1");
        assertEquals(expected, preset);
    }

    @Test
    public final void testGetFailedPresetsFromResultSet() throws SQLException {
        MockResultSet result = statementHandler.createResultSet();
        assertNull(database.getIPCameraPresetFromResultSet(result));
    }
    
    @Test
    public final void testGetFailedSimplePresetsFromResultSet() throws SQLException {
        MockResultSet result = statementHandler.createResultSet();
        assertNull(database.getSimplePresetsFromResultSet(result));
    }

    @Test
    public final void testFailedDeleteCamera() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.deleteCamera(1);
        verify(logger).log("Cameras could not be deleted from database.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testFailedDeletePreset() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        Preset preset = mock(Preset.class);
        database.deletePreset(preset);
        verify(logger).log("Presets could not be deleted.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testFailedGetPresets() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.getAllPresets();
        database.getAllPresetsCamera(1);
        verify(logger, atLeast(2)).log("Presets could not be gotten.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testFailedConnectionClose() throws SQLException {
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).close();
        database.setConnection(connection);
        database.closeConnection();
        verify(logger).log("Database connection couldn't be closed.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testFailedCheckCameras() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.checkCameras(new ArrayList<>());
        verify(logger).log("Cameras could not be gotten from database.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testCheckNewCameras() throws CameraConnectionException {
        Camera camera = mock(Camera.class);
        when(camera.getMacAddress()).thenReturn("mas");
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

    @Test
    public final void testFailedUseDatabase() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.useDatabase();
        verify(logger).log("Database could not be found.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testAddTag() throws SQLException {
        database.resetDatabase();
        database.addTag("tag1");
        database.closeConnection();
        verifySQLStatementExecuted("INSERT INTO tag VALUES('tag1')");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testDeleteTag() throws SQLException {
        database.resetDatabase();
        database.addTag("tag1");
        database.deleteTag("tag1");
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM tag WHERE name = 'tag1'");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetTags() throws SQLException {
        database.resetDatabase();
        database.getTags();
        database.closeConnection();
        verifySQLStatementExecuted("SELECT name FROM tag");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testFailedAddTag() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.addTag("tag1");
        verify(logger).log("Tag couldn't be added.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testFailedDeleteTag() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.deleteTag("tag1");
        verify(logger).log("Tag couldn't be deleted.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testFailedGetTags() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.getTags();
        verify(logger).log("Tag couldn't be gotten.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testAddTagToPreset() throws SQLException {
        Preset preset = getPreset();
        database.resetDatabase();
        database.addTagToPreset("tag1", preset);
        database.closeConnection();
        verifySQLStatementExecuted("INSERT INTO tagPresets VALUES(");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testDeleteTagFromPreset() throws SQLException {
        Preset preset = getPreset();
        database.resetDatabase();
        database.addTagToPreset("tag1", preset);
        database.deleteTagFromPreset("tag1", preset);
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM tagPresets WHERE tag_name = tag1");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetTagsFromPreset() throws SQLException {
        Preset preset = getPreset();
        database.resetDatabase();
        database.getTagsFromPreset(preset);
        database.closeConnection();
        verifySQLStatementExecuted("SELECT tag_name FROM tagPresets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testFailedAddTagToPreset() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.addTagToPreset("tag1", null);
        verify(logger).log("Tag couldn't be added.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testFailedDeleteTagFromPreset() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.deleteTagFromPreset("tag1", null);
        verify(logger).log("Tag couldn't be deleted.", LogEvent.Type.CRITICAL);
    }

    @Test
    public final void testFailedGetTagsFromPreset() throws SQLException {
        database.closeConnection();
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).createStatement();
        database.setConnection(connection);
        database.getTagsFromPreset(null);
        verify(logger).log("Tags could not be gotten.", LogEvent.Type.CRITICAL);
    }

    public IPCameraPreset getPreset() {
        return new IPCameraPreset(new ZoomPosition(1, 1, 1), 1, 1, true, false, 0);
    }

}