package com.passwordkeeper.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubscriptionRequestDto {

    private String userId;
    private SubscriptionPeriod subscriptionPeriod;
    private String subscriptionId;
    private String paymentId;
}
