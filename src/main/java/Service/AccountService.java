package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO actDAO;

    public AccountService(){
        actDAO = new AccountDAO();
    }

    public AccountService(AccountDAO actDAO){
        this.actDAO = actDAO;
    }

    public Account addAccount(Account act){
        if(4 >= act.getPassword().length()){
            return null;
        }else if(0 == act.getUsername().length()){
            return null;
        }else{
            return actDAO.registerAct(act);
        }

    }

    public Account loginAccount(Account act){
        return actDAO.loginAct(act);
    }
}
