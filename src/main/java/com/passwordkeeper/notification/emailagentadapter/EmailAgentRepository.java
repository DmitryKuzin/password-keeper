package com.passwordkeeper.notification.emailagentadapter;

public interface EmailAgentRepository {

    boolean sendEmail(EmailDto emailDto);
}
