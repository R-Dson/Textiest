package Managers;

import Data.Credential;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager {

    private final String _fullURL;
    private final String _ID;
    private final String _ConnectionCredential;

    public SQLManager(Credential credential){
        _ID = credential.ID;
        _ConnectionCredential = credential.ConnectionCredential;
        _fullURL = credential.ServerString + _ID;

        System.out.println("SQL string: " + _fullURL);
    }

    //returns ecrypted password
    public String requestUsernameExist(String username){
        try(Connection connection = DriverManager.getConnection(_fullURL, _ID, _ConnectionCredential))
        {
            String query = "SELECT accountinfosecond FROM Users WHERE accountInfo = ? ";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next())
            {
                String returns = resultSet.getString(1);
                System.out.println(returns);
                pstmt.close();
                return returns;
            }
            else{
                pstmt.close();
                return null;
            }

        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //Only requestable after login and username exists
    public String requestData(String username){
        try(Connection connection = DriverManager.getConnection(_fullURL, _ID, _ConnectionCredential))
        {
            String query = "SELECT accountData FROM Users WHERE accountInfo = ? ";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            String data = resultSet.getString(1);
            System.out.println(data);
            return data;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //ENCRYPTED PASSWORD. data as json
    public void addData(String username, String password, String data){
        //add if returns null
        if (requestUsernameExist(username) == null){
            try(Connection connection = DriverManager.getConnection(_fullURL, _ID, _ConnectionCredential)) {
                String query = "INSERT INTO Users VALUES (?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, data);
                pstmt.setString(3, password);
                pstmt.execute();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

    public void UpdateData(String username, String data){
        try(Connection connection = DriverManager.getConnection(_fullURL, _ID, _ConnectionCredential))
        {
            String query = "UPDATE Users SET accountData = ? WHERE accountInfo = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, data);
            pstmt.setString(2, username);
            pstmt.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
