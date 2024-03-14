package com.passwordkeeper.notification;

import com.passwordkeeper.notification.emailagentadapter.EmailAgentRepository;
import com.passwordkeeper.notification.emailagentadapter.EmailDto;
import com.passwordkeeper.user.UserDto;
import com.passwordkeeper.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailClientNotificationService implements ClientNotificationService{

    @Autowired
    private UserService userService;
    @Autowired
    private EmailAgentRepository emailAgentRepository;

    @Override
    public boolean notify(String userId, String message) {
        UserDto userDto = userService.findById(userId);
        return emailAgentRepository.sendEmail(new EmailDto(userDto.getLogin(), message));
    }
}
