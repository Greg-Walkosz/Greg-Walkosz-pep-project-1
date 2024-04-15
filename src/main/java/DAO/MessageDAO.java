package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public List<Message> getAllMsgs(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> msg = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message";

            PreparedStatement pS = connection.prepareStatement(sql);
            ResultSet rs = pS.executeQuery();
            while(rs.next()){
                Message temp = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                msg.add(temp);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return msg;
    }

    public Message getMsgById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message temp = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                return temp;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMsgByUser(int user){
        List<Message> msg = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,user);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message temp = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                msg.add(temp);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return msg;
    }

    public Message createMsg(Message msg){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1,msg.getPosted_by());
            preparedStatement.setString(2,msg.getMessage_text());
            preparedStatement.setLong(3,msg.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while(rs.next()){
                int randNum = (int) rs.getLong(1);

                return new Message(randNum, msg.getPosted_by(),msg.getMessage_text(),msg.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void updateMsg(int id, Message msg){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,msg.getMessage_text());
            preparedStatement.setInt(2,id);

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteMsg(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,id);

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
