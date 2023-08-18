package com.passwordkeeper.password;

import com.passwordkeeper.subscription.SubscriptionService;
import com.passwordkeeper.user.*;
import com.passwordkeeper.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PasswordService {

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    public List<PasswordDto> getAllPasswordsByUserId(String userId) {
        List<PasswordDto> result = new ArrayList<>();
        passwordRepository.findAllByUserId(userId).forEach(password -> {
            if (password != null && password.getPassword() != null && password.getWebsiteName() != null) {
                result.add(PasswordDto.builder().password(password.getPassword()).website_name(password.getWebsiteName()).userId(password.getUserId()).build());
            }
        });
        return result;
    }

    public boolean savePassword(PasswordDto dto) {
        try {
            boolean notIncremented = !userService.passwordsCountIncremented(dto.getUserId());
            if (notIncremented) {
                return false;
            }
            passwordRepository.save(
                    PasswordEntity.builder()
                            .password(dto.getPassword())
                            .websiteName(dto.getWebsite_name())
                            .userId(dto.getUserId())
                            .build()
            );
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public String getPasswordByWebsiteNameAndUserId(String websiteName, String userId) {
        if (websiteName != null && !websiteName.isEmpty()) {
            PasswordEntity password = passwordRepository.findPasswordByWebsiteNameAndUserId(websiteName, userId);
            if (password != null) {
                return password.getPassword();
            }
        }
        return null;
    }

    @Transactional
    public boolean deleteByWebsiteNameAndUserId(String websiteName, String userId) {
        try {
            passwordRepository.deletePasswordByWebsiteNameAndUserId(websiteName, userId);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Transactional
    public boolean deleteAllByUserId(String userId) {
        try {
            passwordRepository.deleteAllByUserId(userId);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
