package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.RegisterDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationModel {
    public boolean update(RegisterDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE user SET user_name = ?, password = ? WHERE phone_number = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getUser_name());
        pstm.setString(2,dto.getPassword());
        pstm.setInt(3,dto.getPhone_number());

        return pstm.executeUpdate() > 0;
    }

    public RegisterDto search(String phoneNumber) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE phone_number = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,phoneNumber);
        ResultSet resultSet = pstm.executeQuery();

        RegisterDto dto = null;
        if (resultSet.next()) {
            String name = resultSet.getString(1);
            int phone_number = resultSet.getInt(2);
            String pw = resultSet.getString(3);

            dto = new RegisterDto(name,phone_number,pw);
        }
        return dto;
    }

    public boolean check(int phone) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) FROM user WHERE phone_number = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setInt(1, phone);

        ResultSet resultSet = ptsm.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0; // If count is greater than 0, it means the phone number is duplicated
        } else {
            return false; // No results, so phone number is not duplicated
        }
    }

    public boolean save(RegisterDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO user VALUES (?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,dto.getUser_name());
        pstm.setInt(2,dto.getPhone_number());
        pstm.setString(3, dto.getPassword());

        return pstm.executeUpdate() > 0;
    }

    public boolean isValidUser(String name, String pw) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE user_name = ? AND password = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, name);
        ptsm.setString(2,pw);

        ResultSet resultSet = ptsm.executeQuery();

        return resultSet.next();
    }

    public RegisterDto getUserInfo(String name) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM user WHERE user_name = ?";
        try (PreparedStatement ptsm = connection.prepareStatement(sql)) {
            ptsm.setString(1, name);

            try (ResultSet resultSet = ptsm.executeQuery()) {
                if (resultSet.next()) {
                    String retrievedUserName = resultSet.getString("user_name");
                    int phoneNumber = Integer.parseInt(resultSet.getString("phone_number"));
                    String retrievedPassword = resultSet.getString("password");

                    return new RegisterDto(retrievedUserName,phoneNumber, retrievedPassword);
                }
            }
        }
        return null; // User isn't found
    }
}
