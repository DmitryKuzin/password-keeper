package com.passwordkeeper.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("subscriptions")
public class SubscriptionRestController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<SubscriptionDto> getSubscriptionByUSerId(@RequestParam("user_id") String userId) {
        SubscriptionDto subscription = subscriptionService.getSubscriptionByUserId(userId);
        if (subscription.getStatus() != SubscriptionStatus.NOT_FOUND) {
            return ResponseEntity.ok(subscription);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> requestSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        SubscriptionDto subscriptionDto = subscriptionService.requestSubscriptionCreation(subscriptionRequestDto);
        if (subscriptionDto.getStatus() == SubscriptionStatus.ENABLED) {
            return ResponseEntity.badRequest().body(subscriptionDto);
        }
        return ResponseEntity.ok(subscriptionDto);
    }

    @GetMapping("/activation")
    public ResponseEntity<SubscriptionDto> checkSubscription(@RequestParam("payment_id") String paymentId,
                                             @RequestParam("subscription_id") String subscriptionId) {
        return ResponseEntity.ok(subscriptionService.checkPayment(
                SubscriptionRequestDto.builder()
                        .paymentId(paymentId)
                        .subscriptionId(subscriptionId)
                        .build()
        ));
    }

    @DeleteMapping("subscriptions")
    public ResponseEntity<SubscriptionDto> deleteSubscription(@RequestParam("user_id") String userId) {
        return ResponseEntity.ok(subscriptionService.requestSubscriptionDeletion(userId));
    }
}
