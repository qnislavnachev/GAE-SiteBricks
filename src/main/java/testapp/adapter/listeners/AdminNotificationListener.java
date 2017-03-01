package testapp.adapter.listeners;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import testapp.core.SendGrid;
import testapp.core.User;
import testapp.core.UserListener;

import java.util.List;

public class AdminNotificationListener implements UserListener {
    private SendGrid mail;

    public AdminNotificationListener() {
        mail = new SendGrid("qnislav.nachev", "qwerty123123");
    }

    @Override
    public void onRegistered(User user) {
        try {
            mail.setTo("qnislav.nachev@gmail.com");
            mail.setFrom("iani_ty2@abv.bg");
            mail.setSubject("User was registered");
            mail.setText("New User was registered: " + user.getName() + "!");
            mail.send();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRegistered(List<User> userList) {

    }

    @Override
    public void onDeleted(List<User> userList) {

    }
}
