package com.gft.tag_ms.exception;


import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {


    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
