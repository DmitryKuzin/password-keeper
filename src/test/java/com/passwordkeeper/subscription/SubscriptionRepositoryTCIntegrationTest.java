package com.passwordkeeper.subscription;

import com.passwordkeeper.TestPostgresqlContainer;
import com.passwordkeeper.utils.DateUtils;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionRepositoryTCIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer<TestPostgresqlContainer> postgreSQLContainer = TestPostgresqlContainer.getInstance();

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    public void should_save_subscription() {
        Subscription saved = subscriptionRepository.save(
                Subscription.builder()
                        .paymentId("123")
                        .userId("1")
                        .status(SubscriptionStatus.PAYMENT_IN_PROCESS.name())
                        .expirationDate(new Date()).build());

        assertThat(saved).isNotNull();
        assertThat(saved.getSubscriptionId()).isNotNull();

        Subscription byUserId = subscriptionRepository.findByUserId("1");

        assertThat(byUserId).isNotNull();
        assertThat(byUserId.getSubscriptionId()).isEqualTo(saved.getSubscriptionId());


    }
}
