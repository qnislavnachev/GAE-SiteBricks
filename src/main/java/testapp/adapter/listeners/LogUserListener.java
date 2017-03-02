package testapp.adapter.listeners;

import testapp.core.User;
import testapp.core.UserListener;

public class LogUserListener implements UserListener {

    @Override
    public void onRegister(User user) {
        System.out.println("user was registered");
    }
}
