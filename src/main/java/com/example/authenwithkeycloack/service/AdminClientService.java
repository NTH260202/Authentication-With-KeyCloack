package com.example.authenwithkeycloack.service;

import com.example.authenwithkeycloack.UserDTO;
import com.example.authenwithkeycloack.config.keycloak.KeycloakProvider;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;

@Service
@Slf4j
public class AdminClientService {
    @Value("${keycloak.realm}")
    public String realm;

    @Autowired
    private KeycloakProvider keyCloakProvider;

    public Response createKeycloakUser(UserDTO user) {
        UsersResource usersResource = keyCloakProvider.getAdminMaster().realm(realm).users();
        RealmResource realmResource = keyCloakProvider.getAdminMaster().realm(realm);
        ClientResource clientResource = keyCloakProvider.getAdminMaster().realm(realm).clients().get("spring-sec-with-keycloak");
        String resourceId = keyCloakProvider.getAdminMaster().realm("thanhha").clients().findByClientId("spring-sec-with-keycloak").get(0).getId();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());


        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            //If you want to save the user to your other database, do it here, for example:
//            User localUser = new User();
//            localUser.setFirstName(kcUser.getFirstName());
//            localUser.setLastName(kcUser.getLastName());
//            localUser.setEmail(user.getEmail());
//            localUser.setCreatedDate(Timestamp.from(Instant.now()));
//            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
//            usersResource.get(userId).sendVerifyEmail();
//            userRepository.save(localUser);
            String userId = CreatedResponseUtil.getCreatedId(response);
            UserResource userResource = usersResource.get(userId);

//            RoleRepresentation realmRoleUser = realmResource.roles().get(user.getRole()).toRepresentation();
            RoleRepresentation clientRoleUser = clientResource.roles().get(user.getRole()).toRepresentation();
            // Assign realm role student to user
            userResource.roles().clientLevel(resourceId).add(Arrays.asList(clientRoleUser));
//            userResource.roles().realmLevel().add(Arrays.asList(realmRoleUser));
        }
        return response;

    }
    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
