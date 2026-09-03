package service;

import model.User;
import repository.UserRepository;

public class UserService {

    private final UserRepository repository;

    public UserService(
            UserRepository repository) {

        this.repository =
                repository;
    }

    public User getUser(
            int id) {

        return repository.findById(
                id
        );
    }

    public User createUser(
            int id,
            String name) {

        User user =
                new User(
                        id,
                        name
                );

        repository.save(
                user
        );

        return user;
    }
}