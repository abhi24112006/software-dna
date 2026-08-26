package repository;

import model.User;
import service.UserService;

public class UserRepository {

    private final UserService service;

    public UserRepository(
        UserService service) {

    this.service = service;
}

    public User findById(
            int id) {

        return new User(
                id,
                "User-" + id
        );
    }

    public void save(
            User user) {

        System.out.println(
                "Saving user: "
                        + user.getName()
        );
    }
}