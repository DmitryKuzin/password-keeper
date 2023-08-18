package com.passwordkeeper.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UserRestController.class})
public class UsersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void should_return_user_1_info_by_id_and_return_200() throws Exception {
        String userId = "1";

        when(userService.findById(userId)).thenReturn(UserDto.builder().id(userId).passwordsCount(0).accountType(UserAccountType.FREE).login("login").build());

        mockMvc.perform(get("/users/" + userId)).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
                "{\"id\":\"1\",\"login\":\"login\",\"passwordsCount\":0,\"accountType\":\"FREE\"}"));
    }

    @Test
    void should_save_user_and_return_200() throws Exception {
        UserRequestDto createUserRequest = new UserRequestDto("login", "12345");

        when(userService.findByLogin("login")).thenReturn(null);
        when(userService.create(createUserRequest)).thenReturn(new UserDto("1", "login", 0, UserAccountType.FREE));

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"login\" : \"login\"," +
                                "\"password\" : \"12345\"" +
                                "}")
                ).andDo(print()).andExpect(status().isOk()).andExpect(content().string("1"));
    }

    @Test
    void should_not_save_user_and_return_error_and_return_200() throws Exception {

        when(userService.findByLogin("login")).thenReturn(new UserDto("1", "login", 0, UserAccountType.FREE));
        verify(userService, never()).create(any());

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"login\" : \"login\"," +
                                "\"password\" : \"12345\"" +
                                "}")
        ).andDo(print()).andExpect(status().isBadRequest()).andExpect(content().string("User with login \"login\" already exists in database!"));
    }

}
