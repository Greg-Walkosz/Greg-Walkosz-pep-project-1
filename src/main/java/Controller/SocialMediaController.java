package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService actService;
    MessageService msgService;

    public SocialMediaController(){
        this.actService = new AccountService();
        this.msgService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::userRegistrationHandler);
        app.post("/login", this::userLoginHandler);
        app.post("/messages", this::createMsgHandler);
        app.get("/messages", this::getAllMsgsHandler);
        app.get("/messages/{message_id}", this::getMsgByMsgIdHandler);
        app.delete("/messages/{message_id}", this::deleteMsgByMsgIdHandler);
        app.patch("/messages/{message_id}", this::updateMsgByMsgIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMsgsByActIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void userRegistrationHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account act = mapper.readValue(ctx.body(), Account.class);
        Account regAct = actService.addAccount(act);
        if(regAct!=null){
            ctx.json(mapper.writeValueAsString(regAct));
        }else{
            ctx.status(400);
        }
    }

    private void userLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account act = mapper.readValue(ctx.body(), Account.class);
        Account logAct = actService.loginAccount(act);
        if(logAct!=null){
            ctx.json(mapper.writeValueAsString(logAct));
        }else{
            ctx.status(401);
        }
    }

    private void createMsgHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(ctx.body(), Message.class);
        Message crtmsg = msgService.createMessage(msg);
        if(crtmsg!=null){
            ctx.json(mapper.writeValueAsString(crtmsg));
        }else{
            ctx.status(400);
        }
    }

    private void getAllMsgsHandler(Context ctx) throws JsonProcessingException {
        ctx.json(msgService.getAllMsgs());
    }

    private void getMsgByMsgIdHandler(Context ctx) throws JsonProcessingException {
        Message getMsg =msgService.getMsgById(Integer.parseInt(ctx.pathParam("message_id")));
        if(getMsg!=null){
            ctx.json(getMsg);
        }else{
            ctx.status(200);
        }
    }

    private void deleteMsgByMsgIdHandler(Context ctx) throws JsonProcessingException {
        Message dltMsg =msgService.deleteMsg(Integer.parseInt(ctx.pathParam("message_id")));
        if(dltMsg!=null){
            ctx.json(dltMsg);
        }else{
            ctx.status(200);
        }
    }

    private void updateMsgByMsgIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(ctx.body(), Message.class);
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updMsg = msgService.updateMsg(msg_id, msg);
        if(updMsg!=null){
            ctx.json(mapper.writeValueAsString(updMsg));
        }else{
            ctx.status(400);
        }
    }

    private void getAllMsgsByActIdHandler(Context ctx) throws JsonProcessingException {
        ctx.json(msgService.getMsgByUser(Integer.parseInt(ctx.pathParam("account_id"))));
    }

}

/*
1 X
User Registration
As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an account_id.
The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
If the registration is not successful, the response status should be 400. (Client error)

2 X
Login
As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account, not containing an account_id. In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.
The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. If successful, the response body should contain a JSON of the account in the response body, including its account_id. The response status should be 200 OK, which is the default.
If the login is not successful, the response status should be 401. (Unauthorized)

3 X
Create New Message
As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.
The creation of the message will be successful if and only if the message_text is not blank, is under 255 characters, and posted_by refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, which is the default. The new message should be persisted to the database.
If the creation of the message is not successful, the response status should be 400. (Client error)
 
4 X
Get All Messages
As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
The response body should contain a JSON representation of a list containing all messages retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.

5 X
Get One Message Given Message Id
As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.
The response body should contain a JSON representation of the message identified by the message_id. It is expected for the response body to simply be empty if there is no such message. The response status should always be 200, which is the default.

6 X
Delete a Message Given Message Id
As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.
The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain the now-deleted message. The response status should be 200, which is the default.
If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.

7 X
Update Message Given Message Id
As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. The request body should contain a new message_text values to replace the message identified by message_id. The request body can not be guaranteed to contain any other information.
The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
If the update of the message is not successful for any reason, the response status should be 400. (Client error)

8 X
Get All Messages From User Given Account Id
As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default
 */