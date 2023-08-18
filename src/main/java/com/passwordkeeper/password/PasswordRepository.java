package com.passwordkeeper.password;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PasswordRepository extends CrudRepository<PasswordEntity, String> {

    List<PasswordEntity> findAllByUserId(String userId);

    PasswordEntity findPasswordByWebsiteNameAndUserId(String websiteName, String userId);

    void deletePasswordByWebsiteNameAndUserId(String websiteName, String userId);

    void deleteAllByUserId(String userId);
}
