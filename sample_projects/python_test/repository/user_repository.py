from model.user import User


class UserRepository:

    def save(self, user: User) -> User:
        return user