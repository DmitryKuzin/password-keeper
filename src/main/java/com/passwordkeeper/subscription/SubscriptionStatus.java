package com.passwordkeeper.subscription;

public enum SubscriptionStatus {
    ENABLED, NOT_FOUND, PAYMENT_IN_PROCESS,
    //just for user representation/ not saved in database
    DISABLED;
}
