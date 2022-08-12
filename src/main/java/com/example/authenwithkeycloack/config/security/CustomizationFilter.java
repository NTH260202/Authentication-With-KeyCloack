package com.example.authenwithkeycloack.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomizationFilter extends OncePerRequestFilter {
    private final TokenUtils tokenUtils;

    public CustomizationFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    if (StringUtils.hasText(token) && tokenUtils.validateToken(token)) {
//                        //get username from token
//                        String username = tokenUtils.getUsernameFromJwt(token);
//                        //get userDetail
//                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                        if (userDetails != null) {
//                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
//                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//                        }
                        filterChain.doFilter(request, response);
                    }

                } catch (Exception ex) {
                    response.setHeader("error", ex.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", ex.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
