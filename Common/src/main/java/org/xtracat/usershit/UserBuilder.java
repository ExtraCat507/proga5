package org.xtracat.usershit;

//import org.xtracat.security.HashManager;
@Deprecated
public class UserBuilder {
    private String login;
    private String salt;
    private byte [] hash;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password){
     //   PasswordRecord passwordRecord = HashManager.hash(password);
      //  salt = passwordRecord.salt();
  //      hash = passwordRecord.hash();
    }

    public UserRecord build(){
        return new UserRecord(login,salt,hash);
    }

}
