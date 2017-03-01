package testapp.core;

import java.util.List;

public class UserValidator {
    private List<User> userList;

    public UserValidator(List<User> userList) {
        this.userList = userList;
    }

    public boolean validate(String value, String regex) {
       return value.matches(regex);
    }

    public boolean isExist(User user) {
        for (User each : userList) {
            if (each.getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }
}
