package kr.ac.jbnu.se.tetris;
import java.sql.*;

public class ServerSetting {

    protected Connection connection = null;

    ServerSetting() throws SQLException{
        connectMysql();
        connectServer();
    }

    public void connectMysql() {
        try {
            Class.forName(ServerInfo.DRIVER).newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
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