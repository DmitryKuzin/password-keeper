package com.passwordkeeper.password;

import com.passwordkeeper.TestPostgresqlContainer;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordRepositoryTCIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer<TestPostgresqlContainer> postgreSQLContainer = TestPostgresqlContainer.getInstance();

    @Autowired
    private PasswordRepository passwordRepository;

    @Test
    @Transactional
    public void should_save_password() {
        PasswordEntity saved = passwordRepository.save(PasswordEntity.builder().password("123").userId("1").websiteName("vk.com").build());
        assertThat(saved.getId()).isNotNull();

        PasswordEntity passwordByWebsiteNameAndUserId = passwordRepository.findPasswordByWebsiteNameAndUserId("vk.com", "1");

        assertThat(passwordByWebsiteNameAndUserId).isNotNull();
        assertThat(passwordByWebsiteNameAndUserId.getId()).isEqualTo(saved.getId());

        passwordRepository.deleteAllByUserId("1");

        PasswordEntity empty = passwordRepository.findPasswordByWebsiteNameAndUserId("vk.com", "1");
        assertThat(empty).isNull();
    }
}
