package org.xtracat.usershit;

import java.io.Serializable;

public record UserRecord (
        String login,
        String salt,
        byte [] hash
) implements Serializable{
    @Override
    public String toString(){
        return login;
    }
}
