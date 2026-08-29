import User from "../model/User.js";
import UserService from "../service/UserService.js";

class UserController {

    constructor(service) {
        this.service = service;
    }

    create(user) {
        return this.service.createUser(user);
    }
}

export default UserController;