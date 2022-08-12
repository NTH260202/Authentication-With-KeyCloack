package com.example.authenwithkeycloack.controller;

import com.example.authenwithkeycloack.UserDTO;
import com.example.authenwithkeycloack.config.keycloak.KeycloakProvider;
import com.example.authenwithkeycloack.service.AdminClientService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Value("${keycloak.realm}")
    private String realm;

//    @SecurityRequirements
//    @PostMapping("/create")
//    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
//
//            Keycloak keycloak = keycloakConfig.getAdminMaster();
//
//            keycloak.tokenManager().getAccessToken();
//
//            UserRepresentation user = new UserRepresentation();
//            user.setEnabled(true);
//            user.setUsername(userDTO.getUsername());
//            user.setFirstName(userDTO.getFirstname());
//            user.setLastName(userDTO.getLastname());
//            user.setEmail(userDTO.getEmail());
//
//            // Get realm
//            RealmResource realmResource = keycloak.realm(realm);
//            UsersResource usersResource = realmResource.users();
//
//            Response response = usersResource.create(user);
//
//            userDTO.setStatusCode(response.getStatus());
//            userDTO.setStatus(response.getStatusInfo().toString());
//
//            if (response.getStatus() == 201) {
//
//                String userId = CreatedResponseUtil.getCreatedId(response);
//
//
//                // create password credential
//                CredentialRepresentation passwordCred = new CredentialRepresentation();
//                passwordCred.setTemporary(false);
//                passwordCred.setType(CredentialRepresentation.PASSWORD);
//                passwordCred.setValue(userDTO.getPassword());
//
//                UserResource userResource = usersResource.get(userId);
//
//                // Set password credential
//                userResource.resetPassword(passwordCred);
//
//                // Get realm role student
//                try {
//                    RoleRepresentation realmRoleUser = realmResource.roles().get(userDTO.getRole()).toRepresentation();
//                    // Assign realm role student to user
//                    userResource.roles().realmLevel().add(Arrays.asList(realmRoleUser));
//                } catch(NotFoundException exception) {
//                    return new ResponseEntity<>("{\"message\" : \"Role is not found\"}",HttpStatus.NOT_FOUND);
//                }
//            }
//            return ResponseEntity.ok(userDTO);
//    }

    private final AdminClientService kcAdminClient;

    private final KeycloakProvider kcProvider;

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);


    public UserController(AdminClientService kcAdminClient, KeycloakProvider kcProvider) {
        this.kcProvider = kcProvider;
        this.kcAdminClient = kcAdminClient;
    }


    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO user) {
        Response createdResponse = kcAdminClient.createKeycloakUser(user);
        return ResponseEntity.status(createdResponse.getStatus()).build();

    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody UserDTO loginRequest) {
        log.info("Reach login controller");
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.getUsername(), loginRequest.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();

            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            LOG.warn("invalid account. User probably hasn't verified email.", ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }

    }
}
