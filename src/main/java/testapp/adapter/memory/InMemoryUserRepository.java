package testapp.adapter.memory;

import testapp.core.User;
import testapp.core.UserAlreadyExistsException;
import testapp.core.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {
    private List<User> userList = new ArrayList<>();

    @Override
    public List<User> findUsers() {
        return userList;
    }

    @Override
    public void register(User user) {
        for (User each : userList) {
            if (each.getId() == user.getId()) {
                throw new UserAlreadyExistsException();
            }
        }
        userList.add(user);
    }

    @Override
    public void delete(int userId) {
        for (User each : userList) {
            if (each.getId() == userId) {
                userList.remove(each);
                return;
            }
        }
    }
}
