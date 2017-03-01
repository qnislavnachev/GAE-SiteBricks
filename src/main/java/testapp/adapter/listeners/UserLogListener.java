package testapp.adapter.listeners;

import testapp.core.User;
import testapp.core.UserListener;

import java.util.List;
import java.util.logging.Logger;

public class UserLogListener implements UserListener {
    private Logger log = Logger.getLogger(UserLogListener.class.getName());

    @Override
    public void onRegistered(User user) {
        log.info("User with name: " + user.getName() + " was registered");
    }

    @Override
    public void onRegistered(List<User> userList) {

    }

    @Override
    public void onDeleted(List<User> userList) {

    }
}
