package testapp.core;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

import java.util.List;

public interface UserRepository {

    List<User> findUsers();

    User findById(long ucn);

    Key getEntityKey(long ucn);

    Entity register(User user);

    Entity delete(long ucn);
}
