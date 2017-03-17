package testapp.core;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

import java.util.List;

public class UserValidator implements UserRepository {
    private UserRepository origin;

    public UserValidator(UserRepository origin) {
        this.origin = origin;
    }

    @Override
    public List<User> findUsers() {
        return origin.findUsers();
    }

    @Override
    public User findById(long ucn) {
        User user = origin.findById(ucn);
        if (user != null) {
            return user;
        }
        throw new UserException("User Not Found");
    }

    @Override
    public Key getEntityKey(long ucn) {
        return origin.getEntityKey(ucn);
    }

    @Override
    public Entity register(User user) {
        if (isUserNameValid(user) && isNotRegistered(user)) {
            return origin.register(user);
        }
        throw new UserException("Invalid name or already exist user with that ID");
    }

    @Override
    public Entity delete(long ucn) {
        if (isRegisteredUserWithId(ucn)) {
            return origin.delete(ucn);
        }
        throw new UserException("User does not registered !");
    }

    /**
     *   -= Validation methods start here =-
     */

    private boolean isRegisteredUserWithId(long ucn) {
        if (origin.findById(ucn) != null) {
            return true;
        }
        return false;
    }

    private boolean isNotRegistered(User user) {
       if (origin.findById(user.getUcn()) == null) {
           return true;
       }
       return false;
    }

    private boolean isUserNameValid(User user) {
        return user.getName().matches("^[a-zA-Z]+$");
    }
}
