package org.velihangozek.javarentalcli.service.impl;

import org.velihangozek.javarentalcli.dao.UserDao;
import org.velihangozek.javarentalcli.dao.impl.UserDaoImpl;
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
                         boolean isCorporate) throws Exception {
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
    public Optional<User> login(String email, String plainPassword) throws Exception {
        return userDao.findByEmail(email)
                .filter(u -> u.getPasswordHash().equals(PasswordUtil.hash(plainPassword)));
    }
}
