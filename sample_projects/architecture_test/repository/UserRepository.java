package repository;

import model.User;

public class UserRepository {

    public User findById(int id) {
        return new User(id, "User" + id);
    }

    public void save(User user) {
        System.out.println(
                "Saving user: " + user.getName()
        );
    }
}