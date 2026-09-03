package user;

public class UserService {

    private final UserRepository repository;


    public UserService(
            UserRepository repository) {

        this.repository =
                repository;

    }


    public String getUser(
            int id) {

        return repository.findUser(
                id
        );

    }

}