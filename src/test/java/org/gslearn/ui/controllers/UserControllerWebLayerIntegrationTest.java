package org.gslearn.ui.controllers;

import org.gslearn.security.SecurityConstants;
import org.gslearn.ui.response.UserRest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerWebLayerIntegrationTest {

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    public void testCreateUserWhenValidDetailsProvidedShouldReturnsUserDetails() throws JSONException {
        //Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("firstName", "Gomathi");
        userDetailsRequestJson.put("lastName", "Shankar");
        userDetailsRequestJson.put("email", "test@test.com");
        userDetailsRequestJson.put("password", "test123456");
        userDetailsRequestJson.put("repeatPassword", "test123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString(), headers);


        //Act
        ResponseEntity<UserRest> responseEntity = testRestTemplate.postForEntity("/users", request, UserRest.class);
        UserRest userDetails = responseEntity.getBody();

        //Assert
        Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("Gomathi", userDetails.getFirstName());
    }

    @Test
    @DisplayName("Get List of User")
    @Order(2)
    public void testGetListOfUsersWhenValidDetailsProvidedShouldReturnsUserDetails() {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        //Act
        ResponseEntity<List<UserRest>> responseEntity = testRestTemplate.exchange("/users", HttpMethod.GET, request, new ParameterizedTypeReference<List<UserRest>>() {
        });

        //Assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode(), "Forbidden should be returned");
    }

    @Test
    @DisplayName("Login should work")
    @Order(3)
    void testUserLoginWhenValidCredentialsProvidedShouldReturnJWTinAuthorizationHeader() throws JSONException {

        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("email", "test@test.com");

        userDetailsRequestJson.put("password", "test123456");

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString());

        //Act
        ResponseEntity responseEntity = testRestTemplate.postForEntity("/users/login",
                request,
                null);

        jwtToken = responseEntity.getHeaders().getValuesAsList(SecurityConstants.HEADER_STRING).getFirst();

        //Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Invalid Status Code Should be returned");
        Assertions.assertNotNull(responseEntity.getHeaders().getValuesAsList(SecurityConstants.HEADER_STRING).getFirst(), "Response should contain authorization with JWT");
    }

    @Test
    @Order(4)
    @DisplayName("Get user list with valid Jwt Token")
    public void testGetListOfUsersWhenValidCredentialsAndJWTTokenProvidedShouldReturnsUserDetails() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        //Act
        ResponseEntity<List<UserRest>> responseEntity = testRestTemplate.exchange("/users", HttpMethod.GET, request, new ParameterizedTypeReference<List<UserRest>>() {
        });

        //Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Forbidden should be returned");
    }
}
