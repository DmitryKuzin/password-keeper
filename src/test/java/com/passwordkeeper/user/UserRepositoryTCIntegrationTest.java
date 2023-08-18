package com.passwordkeeper.user;

import com.passwordkeeper.TestPostgresqlContainer;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTCIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer<TestPostgresqlContainer> postgreSQLContainer = TestPostgresqlContainer.getInstance();

    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_save_user_and_return_user_by_id() {
        UserEntity saved = userRepository.save(UserEntity.builder().login("login").password("1234").passwordsCount(0).accountType("FREE").build());
        assertThat(saved.getId()).isNotNull();

        Optional<UserEntity> result = userRepository.findById(saved.getId());

        assertThat(result.isPresent()).isTrue();
        UserEntity userEntity = result.get();
        assertThat(userEntity.getLogin()).isEqualTo("login");
        assertThat(userEntity.getPassword()).isEqualTo("1234");


        UserEntity byLogin = userRepository.findByLogin(userEntity.getLogin());
        assertThat(byLogin).isNotNull();
        assertThat(byLogin.getId()).isEqualTo(userEntity.getId());
    }

}
