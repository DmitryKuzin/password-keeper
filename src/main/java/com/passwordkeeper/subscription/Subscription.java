package com.passwordkeeper.subscription;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Subscription {

    @Id
    private String subscriptionId;
    private String userId;
    private String status;
    private Date expirationDate;
    private String paymentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subscription that = (Subscription) o;
        return subscriptionId != null && Objects.equals(subscriptionId, that.subscriptionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
