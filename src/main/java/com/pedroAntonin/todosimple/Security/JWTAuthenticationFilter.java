package com.pedroAntonin.todosimple.Security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroAntonin.todosimple.Models.User;
import com.pedroAntonin.todosimple.exceptions.GlobalExceptionHandler;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private AuthenticationManager authenticationManager;

    private JWTUtil jWTUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jWTUtil) {
        setAuthenticationFailureHandler(new GlobalExceptionHandler());
        this.authenticationManager = authenticationManager;
        this.jWTUtil = jWTUtil;
    }

    @Override
    public Authentication attempAuthentication(HttpServletRequest request, 
            HttpServletResponse response) throws AuthenticationException {
        try {
            User userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList<>());

            Authentication authentication = this.authenticationManager.authenticate(authToken);
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain, Authentication authentication)
            throws IOException, ServletException {
        UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();
        String username = userSpringSecurity.getUsername();
        String Token = this.jWTUtil.GenerationToken(username);
        response.addHeader("Authorization", "Bearer" + Token);
        response.addHeader("access-control-expose-headers", "Authorization");
    }
}
