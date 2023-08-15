package com.passwordkeeper.subscription;

import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, String> {

    Subscription findByUserId(String userId);
}
