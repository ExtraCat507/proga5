package org.xtracat.usershit;

import java.io.Serializable;

public record User(
        String login,
        String password
) implements Serializable {
}
