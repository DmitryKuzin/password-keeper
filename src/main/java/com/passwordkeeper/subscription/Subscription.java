package com.passwordkeeper.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Subscription {

    @Id
    private String subscriptionId;
    private String userId;
    private String status;
    private Date expirationDate;
    private String paymentId;

}
