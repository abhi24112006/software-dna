package api;

import model.User;
import service.UserService;

public class UserController {

    private UserService service;

    public UserController(
            UserService service) {

        this.service = service;
    }

    public User handleRequest(int id) {

        return service.getUser(id);
    }

    public void createUser(
            int id,
            String name) {

        service.createUser(id, name);
    }
}