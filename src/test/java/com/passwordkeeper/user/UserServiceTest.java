package com.passwordkeeper.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.passwordkeeper.user.UserAccountType.FREE;
import static com.passwordkeeper.user.UserAccountType.PREMIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Captor
    ArgumentCaptor<UserEntity> userArgumentCaptor;

    @Test
    public void should_return_existing_user_by_id() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity(1L, "login", "124", 0, FREE.name(), false, false, true)));

        UserDto byId = userService.findById("1");

        verify(userRepository, times(1)).findById(1L);

        assertThat(byId).isNotNull();
        assertThat(byId.getId()).isEqualTo("1");
    }

    @Test
    public void should_return_null_for_non_existing_user() {

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        UserDto byId = userService.findById("2");

        verify(userRepository, times(1)).findById(2L);
        assertThat(byId).isNull();
    }

    @Test
    public void should_update_user_account_type_by_id() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity(1L, "login", "124", 0, FREE.name(), false, false, true)));

        userService.updateUserAccountTypeByUserId("1", PREMIUM);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        UserEntity userPassedToRepositoryWithPremiumAccountType = userArgumentCaptor.getValue();
        assertThat(userPassedToRepositoryWithPremiumAccountType.getId()).isEqualTo(1L);
        assertThat(userPassedToRepositoryWithPremiumAccountType.getAccountType()).isEqualTo(PREMIUM.name());


        userService.updateUserAccountTypeByUserId("1", FREE);
        verify(userRepository, times(2)).save(userArgumentCaptor.capture());
        UserEntity userPassedToRepositoryWithFreeAccountType = userArgumentCaptor.getValue();
        assertThat(userPassedToRepositoryWithFreeAccountType.getId()).isEqualTo(1L);
        assertThat(userPassedToRepositoryWithFreeAccountType.getAccountType()).isEqualTo(FREE.name());

    }

    @Test
    public void should_find_user_by_login() {

        when(userRepository.findByLogin("login")).thenReturn(new UserEntity(1L, "login", "124", 0, FREE.name(), false, false, true));

        UserDto login = userService.findByLogin("login");

        verify(userRepository, times(1)).findByLogin(anyString());
        assertThat(login).isNotNull();
        assertThat(login.getId()).isEqualTo("1");

        UserDto wrongLoginUser = userService.findByLogin("login2");

        assertThat(wrongLoginUser).isNull();

    }

    @Test
    public void should_create_user_by_request() {

        when(userRepository.save(any()))
                .thenReturn(new UserEntity(1L, "login", "124", 0, FREE.name(), false, false, true));

        when(passwordEncoder.encode("124")).thenReturn("456");

        UserDto savedUser = userService.create(new UserRequestDto("login", "124"));

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        UserEntity savingUser = userArgumentCaptor.getValue();

        assertThat(savingUser).isNotNull();
        assertThat(savingUser.getAccountType()).isEqualTo(FREE.name());
        assertThat(savingUser.getPasswordsCount()).isEqualTo(0);
        assertThat(savingUser.getPassword()).isEqualTo("456");
        assertThat(savingUser.getLogin()).isEqualTo("login");

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo("1");
    }

    @Test
    public void should_increment_passwords_count() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity(1L, "login", "124", 4, FREE.name(), false, false, true)));
        boolean resultForFreeAccount = userService.passwordsCountIncremented("1");

        assertThat(resultForFreeAccount).isTrue();
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        UserEntity savingValue = userArgumentCaptor.getValue();
        assertThat(savingValue.getPasswordsCount()).isEqualTo(5);

        when(userRepository.findById(2L)).thenReturn(Optional.of(new UserEntity(2L, "login", "124",9999 , PREMIUM.name(), false, false, true)));
        boolean resultForPremiumAccount = userService.passwordsCountIncremented("2");

        assertThat(resultForPremiumAccount).isTrue();
        verify(userRepository, times(2)).save(userArgumentCaptor.capture());
        UserEntity savingValuePremium = userArgumentCaptor.getValue();
        assertThat(savingValuePremium.getPasswordsCount()).isEqualTo(10000);
    }

    @Test
    public void should_not_increment_passwords_count() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity(1L, "login", "124", 5, FREE.name(), false, false, true)));
        boolean resultForFreeAccount = userService.passwordsCountIncremented("1");
        assertThat(resultForFreeAccount).isFalse();
        verify(userRepository, never()).save(any());

        when(userRepository.findById(2L)).thenReturn(Optional.of(new UserEntity(2L, "login", "124",10000 , PREMIUM.name(), false, false, true)));
        boolean resultForPremiumAccount = userService.passwordsCountIncremented("2");
        assertThat(resultForPremiumAccount).isFalse();
        verify(userRepository, never()).save(any());
    }

    @Test
    public void should_throw_the_exception_on_non_existing_user() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userService.passwordsCountIncremented("1");
        });

        assertThat(exception.getMessage()).isEqualTo("Cant find user");
    }


}
