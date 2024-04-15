package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {
    
    public Account registerAct(Account act){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "insert into Account (username, password) values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,act.getUsername());
            preparedStatement.setString(2,act.getPassword());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int gen_id = (int) rs.getLong(1);
                return new Account(gen_id, act.getUsername(), act.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account loginAct(Account act){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Account WHERE username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,act.getUsername());
            preparedStatement.setString(2,act.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Account login = new Account(rs.getInt("account_id"), rs.getString("username"),rs.getString("password"));
                return login;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
