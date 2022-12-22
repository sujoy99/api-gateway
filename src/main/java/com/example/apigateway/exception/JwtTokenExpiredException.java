package com.example.apigateway.exception;

import org.springframework.http.HttpStatus;

import javax.naming.AuthenticationException;

public class JwtTokenExpiredException extends AuthenticationException {
    private static final long serialVersionUID = 1L;
    int status;

    public JwtTokenExpiredException(String msg) {
        super(msg);
        this.status = HttpStatus.GATEWAY_TIMEOUT.value();
    }
}
