package org.velihangozek.javarentalcli.dao;

import org.velihangozek.javarentalcli.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    /**
     * Inserts a new user and returns the generated ID.
     */
    int save(User user) throws Exception;

    /**
     * Finds a user by their ID.
     */
    Optional<User> findById(int id) throws Exception;

    /**
     * Finds a user by email (for login checks).
     */
    Optional<User> findByEmail(String email) throws Exception;

    /**
     * Returns all users.
     */
    List<User> findAll() throws Exception;

    /**
     * Updates an existing user; returns true if any row was affected.
     */
    boolean update(User user) throws Exception;

    /**
     * Deletes a user by ID; returns true if deletion succeeded.
     */
    boolean delete(int id) throws Exception;
}
