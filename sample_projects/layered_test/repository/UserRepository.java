package repository;

import model.User;
import service.UserService;

public class UserRepository {

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


    /*
     * Intentional architecture violation.
     *
     * Repository depends on Service.
     */

    public void refreshCache(
            UserService service) {

        System.out.println(
                "Refreshing cache"
        );

    }

}