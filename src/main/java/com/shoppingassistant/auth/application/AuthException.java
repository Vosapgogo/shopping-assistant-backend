package com.shoppingassistant.auth.application;

/**
 * A failed login, with a machine-readable code so clients can tell "no such account"
 * from "wrong password" instead of parsing message text.
 */
public class AuthException extends RuntimeException {

    public enum Code {
        EMAIL_NOT_FOUND,
        WRONG_PASSWORD
    }

    private final Code code;

    public AuthException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
