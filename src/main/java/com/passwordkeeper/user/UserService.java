package com.passwordkeeper.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.passwordkeeper.user.UserAccountType.FREE;
import static com.passwordkeeper.user.UserAccountType.PREMIUM;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updateUserAccountTypeByUserId(String userId, UserAccountType userAccountType) {
        Optional<UserEntity> byId = userRepository.findById(Long.parseLong(userId));
        if (byId.isPresent()) {
            UserEntity user = byId.get();
            user.setAccountType(userAccountType.name());
            userRepository.save(user);
        }
    }

    public UserDto findById(String userId) {
        return findById(Long.valueOf(userId));
    }

    public UserDto findById(Long userId) {
        Optional<UserEntity> result = userRepository.findById(userId);
        if (result.isPresent()) {
            UserEntity user = result.get();
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
        UserEntity saved = userRepository.save(UserEntity.builder()
                .login(userRequestDto.getLogin())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .passwordsCount(0)
                .accountType(FREE.name())
                .isAccountNonExpired(true)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .build());
        return UserDto.builder()
                .id(String.valueOf(saved.getId()))
                .login(saved.getLogin())
                .accountType(UserAccountType.valueOf(saved.getAccountType()))
                .passwordsCount(saved.getPasswordsCount())
                .build();
    }

    public UserDto findByLogin(String login) {
        UserEntity user = userRepository.findByLogin(login);
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
        Optional<UserEntity> result = userRepository.findById(Long.valueOf(userId));
        if (result.isPresent()) {
            UserEntity user = result.get();
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

    private boolean incrementPasswordsCount(UserEntity user) {
        user.setPasswordsCount(user.getPasswordsCount() + 1);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByLogin(username);
        if (user != null) {
            return UserDetailsDto.builder()
                    .id(user.getId())
                    .username(user.getLogin())
                    .password(user.getPassword())
                    .authorities(Stream.of(user.getAccountType()).map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()))
                    .isAccountNonExpired(user.isAccountNonExpired())
                    .isAccountNonLocked(user.isAccountNonLocked())
                    .isEnabled(user.isEnabled())
                    .build();
        }
        return null;
    }
}
