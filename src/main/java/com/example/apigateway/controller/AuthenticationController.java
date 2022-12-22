package com.example.apigateway.controller;

import com.example.apigateway.dto.AuthenticationStatus;
import com.example.apigateway.dto.ErrorResponseDTO;
import com.example.apigateway.dto.JwtRequest;
import com.example.apigateway.dto.JwtResponse;
import com.example.apigateway.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.apigateway.constants.Constants.API_GATEWAY_PREDICATE;

@RestController
@RequestMapping(value = API_GATEWAY_PREDICATE)
public class AuthenticationController {

    @Autowired
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * * *** NOTE: ***
     * * Api Gateway should match predicate
     * * path to be discoverable in swagger
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        AuthenticationStatus status = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        if (!status.getIsAuthenticated()) {
            List<String> details = new ArrayList<>();
            details.add(status.getMessage());
            ErrorResponseDTO error = new ErrorResponseDTO(new Date(), HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", details, "uri");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private AuthenticationStatus authenticate(String username, String password) {
        AuthenticationStatus status;

        if (!username.equals("foo") && !password.equals("foo")) {
            status = new AuthenticationStatus(false, "Invalid Username/Password");
        } else {
            status = new AuthenticationStatus(true, "Authentication Successful");
        }

        return status;
    }
}
