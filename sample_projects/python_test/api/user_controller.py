from model.user import User
from service.user_service import UserService


class UserController:

    def __init__(self, service: UserService):
        self.service = service

    def create(self, user: User) -> User:
        return self.service.create_user(user)