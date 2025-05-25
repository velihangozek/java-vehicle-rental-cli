package org.velihangozek.javarentalcli.service.impl;

import org.velihangozek.javarentalcli.dao.UserDao;
import org.velihangozek.javarentalcli.dao.impl.UserDaoImpl;
import org.velihangozek.javarentalcli.exception.VeloAuthenticationException;
import org.velihangozek.javarentalcli.model.User;
import org.velihangozek.javarentalcli.service.UserService;
import org.velihangozek.javarentalcli.util.PasswordUtil;

import java.time.LocalDate;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public User register(String email,
                         String plainPassword,
                         String role,
                         LocalDate birthdate,
                         boolean isCorporate) {
        // 1. Hash password
        String hash = PasswordUtil.hash(plainPassword);

        // 2. Build user
        User u = new User(null, email, hash, role, birthdate, isCorporate);

        // 3. Persist and set ID
        int id = userDao.save(u);
        u.setId(id);

        return u;
    }

    @Override
    public User login(String email, String plainPassword) throws VeloAuthenticationException {
        Optional<User> opt;
        try {
            opt = userDao.findByEmail(email);
        } catch (Exception e) {
            // something went wrong at the persistence layer
            throw new VeloAuthenticationException("Authentication service error: " + e.getMessage());
        }

        // now handle invalid credentials
        return opt
                .filter(u -> u.getPasswordHash().equals(PasswordUtil.hash(plainPassword)))
                .orElseThrow(() -> new VeloAuthenticationException("Invalid email or password"));

    }
}