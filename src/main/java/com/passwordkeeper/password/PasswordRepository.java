package com.passwordkeeper.password;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PasswordRepository extends CrudRepository<Password, String> {

    List<Password> findAllByUserId(String userId);

    Password findPasswordByWebsiteNameAndUserId(String websiteName, String userId);

    void deletePasswordByWebsiteNameAndUserId(String websiteName, String userId);

    void deleteAllByUserId(String userId);
}
