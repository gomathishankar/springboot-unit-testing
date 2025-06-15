package org.gslearn.ui.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gslearn.service.UsersService;
import org.gslearn.shared.UserDto;
import org.gslearn.ui.request.UserDetailsRequestModel;
import org.gslearn.ui.response.UserRest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = UsersController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false)
public class UserControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsersService usersService;

    UserDetailsRequestModel userDetailsRequestModel;

    @BeforeEach
    void setUp() {
        userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("Gomathi");
        userDetailsRequestModel.setLastName("Shankar");
        userDetailsRequestModel.setPassword("Test123456");
        userDetailsRequestModel.setRepeatPassword("Test123456");
        userDetailsRequestModel.setEmail("test@test.com");
    }

    @Test
    public void testCreateUserWhenValidUserDetailsAreProvideShouldReturnCreateduser() throws Exception {
        //Arrange

        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());
        when(usersService.createUser(any(UserDto.class))).thenReturn(userDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .accept(String.valueOf(MediaType.APPLICATION_JSON))
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        UserRest userRest = new ObjectMapper().readValue(response, UserRest.class);

        //Assert
        Assertions.assertEquals("Gomathi", userRest.getFirstName(),"First Name is not correct");
        Assertions.assertEquals("Shankar", userRest.getLastName(),"Last Name is not correct");
        Assertions.assertEquals("test@test.com", userRest.getEmail(),"Email is not correct");
        Assertions.assertFalse(userRest.getUserId().isBlank(),"UserId should not be blank");

    }

    @Test
    @DisplayName("First name is not empty")
    public void testCreateUserWhenFirstNameIsEmptyShouldReturn400StatusCode() throws Exception {
        //Arrange

        userDetailsRequestModel.setFirstName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .accept(String.valueOf(MediaType.APPLICATION_JSON))
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        //Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(),"Incorrect status code");

    }

    @Test
    @DisplayName("First name is less than 2 character")
    public void testCreateUserWhenFirstNameIsLessThan2CharacterShouldReturn400StatusCode() throws Exception {
        //Arrange
        userDetailsRequestModel.setFirstName("G");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .accept(String.valueOf(MediaType.APPLICATION_JSON))
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        //Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(),"Incorrect status code");
    }

}
