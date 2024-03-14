package com.passwordkeeper.user;

import com.passwordkeeper.auth.AuthEntryPointJwt;
import com.passwordkeeper.auth.JwtAuthFilter;
import com.passwordkeeper.auth.JwtService;
import com.passwordkeeper.notification.ClientNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private JwtAuthFilter authFilter;

    @MockBean
    JwtService jwtService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    ClientNotificationService clientNotificationService;

    @SpyBean
    AuthEntryPointJwt unauthorizedHandler;

    @Test
    void should_not_return_user_info_and_return_403() throws Exception {

        mockMvc.perform(get("/user/")).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    void should_save_user_and_return_200() throws Exception {
        UserRequestDto createUserRequest = new UserRequestDto("login", "12345");

        when(userService.findByLogin("login")).thenReturn(null);
        when(userService.create(createUserRequest)).thenReturn(new UserDto("1", "login", 0, UserAccountType.FREE));

        mockMvc.perform(
                post("/user")
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
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"login\" : \"login\"," +
                                "\"password\" : \"12345\"" +
                                "}")
        ).andDo(print()).andExpect(status().isBadRequest()).andExpect(content().string("User with login \"login\" already exists in database!"));
    }

}
