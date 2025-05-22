package org.xtracat.usershit;

import com.thoughtworks.xstream.core.util.Pool;
@Deprecated
public class User {
    private String login;
    private int password_hash; // хи-хи )
    private String name;

    public User(String login, String password) {
        this.login = login;
        this.password_hash = password.hashCode();
    }


    public boolean authenticate(String password) {
        return password.hashCode() == password_hash;
    }

}
