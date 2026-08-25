package api;

import model.User;
import service.UserService;
import view.UserView;

public class UserController {

    private final UserService service;

    private final UserView view;


    public UserController(
            UserService service,
            UserView view) {

        this.service =
                service;

        this.view =
                view;

    }


    public void handleRequest(
            int id) {

        User user =
                service.getUser(
                        id
                );

        view.display(
                user
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

        view.display(
                user
        );

    }

}