package com.passwordkeeper.subscription;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SubscriptionCheckService {

    public boolean isSubscriptionValid(Subscription subscription) {
        Date now = new Date();
        return subscription != null && now.getTime() < subscription.getExpirationDate().getTime();
    }
}
