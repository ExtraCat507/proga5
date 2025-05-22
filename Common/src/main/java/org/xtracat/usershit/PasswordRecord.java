package org.xtracat.usershit;

public record PasswordRecord(
        String salt,
        byte[] hash
) {
}
