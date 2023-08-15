package com.passwordkeeper;

import com.passwordkeeper.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(classes = )
public class UsersRestControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void should_return_emply_list_of_users_and_return_200() {
        String userId = "1";

        //when
        ResponseEntity<UserDto> userDtoResponseEntity = restTemplate.getForEntity("/users/" + userId, UserDto.class);

        //then
        assertThat(userDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}
