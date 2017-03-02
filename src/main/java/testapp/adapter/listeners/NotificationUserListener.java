package testapp.adapter.listeners;

import testapp.core.User;
import testapp.core.UserListener;

public class NotificationUserListener implements UserListener {
    @Override
    public void onRegister(User user) {
        System.out.println("you got the notification");
    }
}
