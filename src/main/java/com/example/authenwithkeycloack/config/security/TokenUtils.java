package com.example.authenwithkeycloack.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Slf4j
@Component
public class TokenUtils implements Serializable {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    public boolean validateToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("token",token);
        map.add("client_secret", clientSecret);
        String fooResourceUrl = "http://localhost:8180/realms/thanhha/protocol/openid-connect/token/introspect";
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                fooResourceUrl, request , String.class);
        log.info(response.getBody());
        if (response.getBody().contains("\"active\": false")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean invalidateToken(String token) {

//        HttpHeaders headers = new HttpHeaders();
//        RestTemplate restTemplate = new RestTemplate();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
//        map.add("client_id", clientId);
//        map.add("token",token);
//        map.add("client_secret", clientSecret);
//        String fooResourceUrl = "http://localhost:8180/realms/thanhha/protocol/openid-connect/token/introspect";
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                fooResourceUrl, request , String.class);
//        log.info(response.getBody());
//        if (response.getBody().contains("\"active\": false")) {
//            return false;
//        } else {
//            return true;
//        }
//    }
        return true;
    }
    }
