package org.velihangozek.javarentalcli.service;

import org.velihangozek.javarentalcli.model.User;

import java.time.LocalDate;
import java.util.Optional;

public interface UserService {
    User register(String email, String plainPassword, String role, LocalDate birthdate,
                  boolean isCorporate) throws Exception;

    Optional<User> login(String email, String plainPassword) throws Exception;
}