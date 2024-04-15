package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    MessageDAO msgDAO;

    public MessageService(){
        msgDAO = new MessageDAO();
    }

    public MessageService(MessageDAO msgDAO){
        this.msgDAO = msgDAO;
    }

    public List<Message> getAllMsgs(){
        return msgDAO.getAllMsgs();
    }
    
    public Message getMsgById(int id){
        return msgDAO.getMsgById(id);
    }

    public List<Message> getMsgByUser(int user){
        return msgDAO.getMsgByUser(user);
    }

    public Message createMessage(Message msg){
        if(msg.getMessage_text().length() == 0){
            return null;
        }else{
            return msgDAO.createMsg(msg);
        }
    }

    public Message updateMsg(int id, Message msg){
        if(255 < msg.getMessage_text().length() || 0 == msg.getMessage_text().length()){
            return null;
        }else{
            msgDAO.updateMsg(id, msg);
        }
        return msgDAO.getMsgById(id);
    }

    public Message deleteMsg(int id){
        Message msg = msgDAO.getMsgById(id);
        if(msg!=null){
            msgDAO.deleteMsg(id);
            return msg;
        }else{
            return null;
        }
    }

}
