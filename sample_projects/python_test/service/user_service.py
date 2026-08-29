from model.user import User
from repository.user_repository import UserRepository


class UserService:

    def __init__(self, repository: UserRepository):
        self.repository = repository

    def create_user(self, user: User) -> User:
        return self.repository.save(user)