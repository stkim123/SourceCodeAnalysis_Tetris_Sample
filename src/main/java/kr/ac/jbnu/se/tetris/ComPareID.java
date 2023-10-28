package kr.ac.jbnu.se.tetris;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComPareID{
    private ServerSetting serverSetting;
    private PreparedStatement readQuery;
    private ResultSet matchQuery;
    public static boolean visible = true;
    ComPareID(String id, String pw) throws SQLException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        super();
        Login(id,pw);
    }

    public void Login(String id, String pw) throws SQLException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        serverSetting = new ServerSetting();

        serverSetting.connectMysql();
        serverSetting.connectServer();
        String SQL = "SELECT USER_PW FROM user_info WHERE USER_ID = ?";

        readQuery = serverSetting.connection.prepareStatement(SQL);
        readQuery.setString(SqlTable.USER_ID.ordinal(), id);

        matchQuery = readQuery.executeQuery();
        while(matchQuery.next()){
            try{
            if (matchQuery.getString(SqlTable.USER_ID.ordinal()).contentEquals(pw)) {
                Select home = new Select();
                home.setVisible(true);
                visible = false;
            }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
