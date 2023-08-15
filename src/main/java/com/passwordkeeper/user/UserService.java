package com.passwordkeeper.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.passwordkeeper.user.UserAccountType.FREE;
import static com.passwordkeeper.user.UserAccountType.PREMIUM;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateUserAccountTypeByUserId(String userId, UserAccountType userAccountType) {
        Optional<User> byId = userRepository.findById(Long.parseLong(userId));
        if (byId.isPresent()) {
            User user = byId.get();
            user.setAccountType(userAccountType.name());
            userRepository.save(user);
        }
    }

    public UserDto findById(String userId) {
        Optional<User> result = userRepository.findById(Long.valueOf(userId));
        if (result.isPresent()) {
            User user = result.get();
            return UserDto.builder()
                    .id(String.valueOf(user.getId()))
                    .login(user.getLogin())
                    .passwordsCount(user.getPasswordsCount())
                    .accountType(UserAccountType.valueOf(user.getAccountType()))
                    .build();
        }
        return null;
    }

    public UserDto create(UserRequestDto userRequestDto) {
        User saved = userRepository.save(
                User.builder()
                        .login(userRequestDto.getLogin())
                        .password(userRequestDto.getPassword())
                        .passwordsCount(0)
                        .accountType(FREE.name())
                        .build());
        return UserDto.builder()
                .id(String.valueOf(saved.getId()))
                .login(saved.getLogin())
                .accountType(UserAccountType.valueOf(saved.getAccountType()))
                .passwordsCount(saved.getPasswordsCount())
                .build();
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login);
        if (user != null) {
            return UserDto.builder()
                    .id(String.valueOf(user.getId()))
                    .login(user.getLogin())
                    .passwordsCount(user.getPasswordsCount())
                    .accountType(UserAccountType.valueOf(user.getAccountType()))
                    .build();
        }
        return null;
    }

    public boolean passwordsCountIncremented(String userId) {
        Optional<User> result = userRepository.findById(Long.valueOf(userId));
        if (result.isPresent()) {
            User user = result.get();
            UserAccountType userAccountType = UserAccountType.valueOf(user.getAccountType());
            if (PREMIUM == userAccountType) {
                if (user.getPasswordsCount() < 10000) {
                    return incrementPasswordsCount(user);
                }
            } else if (FREE == userAccountType) {
                if (user.getPasswordsCount() < 5) {
                    return incrementPasswordsCount(user);
                }
            }
            return false;
        }
        throw new IllegalStateException("Cant find user");
    }

    private boolean incrementPasswordsCount(User user) {
        user.setPasswordsCount(user.getPasswordsCount() + 1);
        userRepository.save(user);
        return true;
    }
}
