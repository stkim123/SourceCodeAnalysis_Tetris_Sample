package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpSQL{

    private ServerSetting serverSetting;
    private PreparedStatement readQuery;

    SignUpSQL(String id, String pw) throws SQLException, IOException {
        signUpUser(id,pw);
    }

    public void signUpUser(String id, String pw) throws SQLException, IOException {
        serverSetting = new ServerSetting();

        String SQL = "INSERT INTO user_info(USER_ID, USER_PW) values (?, ?)";

        readQuery = serverSetting.connection.prepareStatement(SQL);

        try {
            readQuery.setString(SqlTable.USER_ID.ordinal(), id);
            readQuery.setString(SqlTable.USER_PW.ordinal(), pw);

            int updatecount = readQuery.executeUpdate();

            if(updatecount >= 1) {
                JOptionPane.showMessageDialog(null, "회원가입 성공");
            }

        } catch (SQLException e) {
            if(e.getMessage().contains("PRIMARY")){
                JOptionPane.showMessageDialog(null, "중복되는 아이디입니다.");
            }
            else {
                JOptionPane.showMessageDialog(null, "제대로 된 정보를 입력해주세요.");
            }
        }finally {
            if(readQuery != null)
                try{
                readQuery.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(serverSetting.connection != null)
                try{
                serverSetting.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
