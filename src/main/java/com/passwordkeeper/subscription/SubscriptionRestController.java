package com.passwordkeeper.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriptionRestController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("subscriptions")
    public SubscriptionDto getSubscriptionByUSerId(@RequestParam("user_id") String userId) {
        return subscriptionService.getSubscriptionByUserId(userId);
    }

    @PostMapping("subscriptions")
    public SubscriptionDto requestSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        return subscriptionService.requestSubscriptionCreation(subscriptionRequestDto);
    }

    @GetMapping("subscriptions/activation")
    public SubscriptionDto checkSubscription(@RequestParam("payment_id") String paymentId,
                                             @RequestParam("subscription_id") String subscriptionId) {
        return subscriptionService.checkPayment(
                SubscriptionRequestDto.builder()
                        .paymentId(paymentId)
                        .subscriptionId(subscriptionId)
                        .build()
        );
    }

    @DeleteMapping("subscriptions")
    public SubscriptionDto deleteSubscription(@RequestParam("user_id") String userId) {
        return subscriptionService.requestSubscriptionDeletion(userId);
    }
}
