package com.passwordkeeper.notification;

public interface ClientNotificationService {

    boolean notify(String userId, String message);
}
