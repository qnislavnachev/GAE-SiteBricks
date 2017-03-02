package testapp.core;

import java.util.List;

public class UserValidator implements UserRepository{
    private List<User> userList;
    private UserRepository origin;

    public UserValidator(List<User> userList, UserRepository origin) {
        this.userList = userList;
        this.origin = origin;
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

    @Override
    public List<User> findUsers() {
        return origin.findUsers();
    }

    @Override
    public User findById(int Id) {
        return origin.findById(Id);
    }

    @Override
    public void register(User user) {
        origin.register(user);
    }

    @Override
    public void delete(int userId) {

    }
}
