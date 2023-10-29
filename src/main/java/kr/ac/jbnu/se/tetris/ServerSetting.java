package kr.ac.jbnu.se.tetris;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerSetting {

    protected Connection connection = null;

    ServerSetting() throws SQLException{
        connectMysql();
        connectServer();
    }

    public void connectMysql() {
        try {
            Class.forName(ServerInfo.DRIVER).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    public void connectServer() throws SQLException {
        connection = DriverManager.getConnection(
                ServerInfo.SERVER_URL,
                ServerInfo.MANAGEMENT_USER,
                ServerInfo.MANAGEMENT_PASSWORD);
    }
}