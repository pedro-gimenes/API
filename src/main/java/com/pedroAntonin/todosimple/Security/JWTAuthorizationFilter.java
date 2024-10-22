package com.pedroAntonin.todosimple.Security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    
    private JWTUtil jWTUtil;

    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jWTUtil,
            UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jWTUtil = jWTUtil;
        this.userDetailsService = userDetailsService;
        }
    @Override
    protected void doFilterInternal(HttpServletRequest reuqest, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authorizationHeader = reuqest.getHeader("Authorization");
        if(Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            UsernamePasswordAuthenticationToken auth = getAuthentication(token);
                if(Objects.nonNull(auth))
                    SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(reuqest, response);
        }

        private UsernamePasswordAuthenticationToken getAuthentication(String token) {
            if(this.jWTUtil.isValidToken(token)) {
                String username = this.jWTUtil.getUsername(token);
                UserDetails user = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                return authenticatedUser;
            }
            return null;
        }

}
