package testapp.core;

import java.util.List;

public interface UserRepository {

    List<User> findUsers();

    void register(User user);

    void delete(int userId);
}
