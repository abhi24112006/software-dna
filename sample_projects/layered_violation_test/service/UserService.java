package service;

import repository.UserRepository;

public class UserService {

    private UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    public User getUser(int id) {
        return userRepository.findUser(id);
    }
}