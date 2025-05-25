package org.velihangozek.javarentalcli.service;

import org.velihangozek.javarentalcli.exception.VeloAuthenticationException;
import org.velihangozek.javarentalcli.model.User;

import java.time.LocalDate;
import java.util.Optional;

public interface UserService {
    /**
     * Registers a new CUSTOMER account.
     */
    User register(String email, String plainPassword, String role, LocalDate birthdate,
                  boolean isCorporate);

    /**
     * Authenticates a user, returning the User or throwing
     * VeloAuthenticationException on failure.
     */
    User login(String email, String plainPassword);
}