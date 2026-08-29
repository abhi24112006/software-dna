import User from "../model/User.js";
import UserRepository from "../repository/UserRepository.js";

class UserService {

    constructor(repository) {
        this.repository = repository;
    }

    createUser(user) {
        return this.repository.save(user);
    }
}

export default UserService;