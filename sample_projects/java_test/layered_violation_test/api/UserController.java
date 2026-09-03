package api;

import model.User;
import service.UserService;

public class UserController {

    private final UserService service;

    public UserController(
            UserService service) {

        this.service =
                service;
    }

    public void handleRequest(
            int id) {

        User user =
                service.getUser(
                        id
                );

        System.out.println(
                "User: "
                        + user.getName()
        );
    }

    public void createUser(
            int id,
            String name) {

        User user =
                service.createUser(
                        id,
                        name
                );

        System.out.println(
                "Created user: "
                        + user.getName()
        );
    }
}