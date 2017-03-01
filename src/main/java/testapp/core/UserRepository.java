package testapp.core;

import java.util.List;

public interface UserRepository {

    List<User> findUsers();

    User findById(int Id);

    void register(User user);

    void delete(int userId);
}
