package testapp.core;

import java.util.List;

public interface UserListener {

    void onRegistered(User user);

    void onRegistered(List<User> userList);

    void onDeleted(List<User> userList);
}
