package com.passwordkeeper.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubscriptionDto {
    private SubscriptionStatus status;
    private String expirationDate;
    private String subscriptionId;
    private String paymentId;
}
