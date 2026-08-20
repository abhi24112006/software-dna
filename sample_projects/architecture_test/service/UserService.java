package service;

import model.User;
import repository.UserRepository;

public class UserService {

    private UserRepository repository;

    public UserService(
            UserRepository repository) {

        this.repository = repository;
    }

    public User getUser(int id) {

        return repository.findById(id);
    }

    public void createUser(
            int id,
            String name) {

        User user =
                new User(id, name);

        repository.save(user);
    }
}